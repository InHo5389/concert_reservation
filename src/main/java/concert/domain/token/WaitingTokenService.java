package concert.domain.token;

import concert.domain.token.dto.WaitingOrderDto;
import concert.domain.token.dto.WaitingTokenIssueTokenDto;
import concert.domain.token.jwt.WaitingTokenProvider;
import concert.domain.token.jwt.WaitingTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static concert.domain.token.TokenStatus.ACTIVE;

@Service
@RequiredArgsConstructor
public class WaitingTokenService {

    private final WaitingTokenRepository waitingTokenRepository;
    private final WaitingTokenProvider tokenProvider;
    private final WaitingTokenValidator tokenValidator;

    private static final int expirationMinutes = 5;
    private static final int fixActiveCount = 2;


    public WaitingTokenIssueTokenDto issueToken(Long userId) {
        LocalDateTime now = LocalDateTime.now();

        int activeCount = waitingTokenRepository.countByTokenStatus(ACTIVE);
        WaitingToken issuedWaitingToken = WaitingToken.issue(now, userId, activeCount, fixActiveCount, expirationMinutes);
        String jwtToken = tokenProvider.issueToken(userId, now, expirationMinutes);
        WaitingToken savedWaitingToken = waitingTokenRepository.save(issuedWaitingToken);

        return new WaitingTokenIssueTokenDto(savedWaitingToken.getId(), savedWaitingToken.getUserId(), savedWaitingToken.getTokenStatus(),
                savedWaitingToken.getCreatedAt(), savedWaitingToken.getExpiredAt(), jwtToken);
    }

    public WaitingOrderDto getWaitingOrder(String jwtToken){
        return tokenValidator.verify(jwtToken);
    }
}
