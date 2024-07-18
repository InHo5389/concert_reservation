package concert.domain.token;

import concert.common.exception.BusinessException;
import concert.domain.token.dto.WaitingOrderDto;
import concert.domain.token.dto.WaitingTokenIssueTokenDto;
import concert.domain.token.jwt.WaitingTokenProvider;
import concert.domain.token.jwt.WaitingTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static concert.domain.token.TokenStatus.*;

@Service
@RequiredArgsConstructor
public class WaitingTokenService {

    private final WaitingTokenRepository waitingTokenRepository;
    private final WaitingTokenProvider tokenProvider;
    private final WaitingTokenValidator tokenValidator;

    private static final int EXPIRATION_MINUTES = 5;
    private static final int FIX_ACTIVE_COUNT = 2;


    public WaitingTokenIssueTokenDto issueToken(Long userId) {

        int activeCount = waitingTokenRepository.countByTokenStatus(ACTIVE);
        WaitingToken issuedWaitingToken = WaitingToken.issue(userId, activeCount, FIX_ACTIVE_COUNT, EXPIRATION_MINUTES);
        WaitingToken savedWaitingToken = waitingTokenRepository.save(issuedWaitingToken);

        String jwtToken = tokenProvider.issueToken(userId,issuedWaitingToken.getCreatedAt(), EXPIRATION_MINUTES);

        return WaitingTokenIssueTokenDto.from(savedWaitingToken, jwtToken);
    }

    public WaitingOrderDto verifyAndGetWaitingOrder(Long userId){
        return calculateWaitingOrder(userId);
    }
//    public WaitingOrderDto verifyAndGetWaitingOrder(String jwtToken){
//        String token = jwtToken.substring(7);
//        Long userId = tokenValidator.validateTokenAndGetUserId(token);
//        return calculateWaitingOrder(userId);
//    }

    private WaitingOrderDto calculateWaitingOrder(Long userId) {
        WaitingToken waitingToken = getWaitingTokenByUserId(userId);

        if (waitingToken.getTokenStatus().equals(ACTIVE)) {
            return WaitingOrderDto.active();
        }else if (waitingToken.getTokenStatus().equals(EXPIRED)) {
            return WaitingOrderDto.invalid();
        }

        long lastActiveTokenNum = waitingTokenRepository.findLastActiveTokenBy(userId);
        long waitingOrder = waitingToken.getId() - lastActiveTokenNum;
        return WaitingOrderDto.waiting(waitingOrder);
    }

    private WaitingToken getWaitingTokenByUserId(Long userId) {
        return waitingTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException("토큰이 존재하지 않습니다."));
    }
}
