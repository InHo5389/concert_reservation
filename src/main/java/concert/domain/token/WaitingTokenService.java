package concert.domain.token;

import concert.common.exception.BusinessException;
import concert.domain.common.TokenUtil;
import concert.domain.token.dto.WaitingOrderDto;
import concert.domain.token.dto.WaitingTokenIssueTokenDto;
import concert.domain.token.entity.WaitingToken;
import concert.domain.token.jwt.WaitingTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static concert.domain.token.TokenStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingTokenService {

    private final WaitingTokenRepository waitingTokenRepository;
    private final WaitingTokenProvider tokenProvider;

    public WaitingTokenIssueTokenDto issueToken(Long userId) {
        log.info("WaitingTokenService issueToken(): userId={}",userId);
        int activeCount = waitingTokenRepository.countByTokenStatus(ACTIVE);
        WaitingToken issuedWaitingToken = WaitingToken.issue(userId, activeCount, TokenUtil.FIX_ACTIVE_COUNT, TokenUtil.EXPIRATION_MINUTES);
        WaitingToken savedWaitingToken = waitingTokenRepository.save(issuedWaitingToken);

        String jwtToken = tokenProvider.issueToken(savedWaitingToken);

        return WaitingTokenIssueTokenDto.from(savedWaitingToken, jwtToken);
    }

    public WaitingOrderDto getWaitingOrder(Long userId){
        log.info("WaitingTokenService verifyAndGetWaitingOrder(): userId={}",userId);
        return calculateWaitingOrder(userId);
    }

    private WaitingOrderDto calculateWaitingOrder(Long userId) {
        WaitingToken waitingToken = getWaitingTokenByUserId(userId);

        if (waitingToken.getTokenStatus().equals(ACTIVE)) {
            log.info("WaitingToken Acive");
            return WaitingOrderDto.active();
        }else if (waitingToken.getTokenStatus().equals(EXPIRED)) {
            log.info("WaitingToken Invalid");
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
