package concert.domain.token;

import concert.domain.token.dto.WaitingOrderDto;
import concert.domain.token.dto.WaitingTokenIssueTokenDto;
import concert.domain.token.jwt.WaitingTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingTokenService {

    private final WaitingTokenRedisRepository waitingTokenRedisRepository;
    private final WaitingTokenProvider tokenProvider;

    public WaitingTokenIssueTokenDto issueToken(Long userId) {
        log.info("WaitingTokenService issueToken(): userId={}", userId);

        // Active 토큰이 있는 경우
        if (waitingTokenRedisRepository.isValidActiveToken(userId)) {
            String jwtToken = tokenProvider.issueToken(userId);
            return WaitingTokenIssueTokenDto.of(userId,TokenStatus.ACTIVE, jwtToken);
        }

        // Waiting 상태인 경우
        if (waitingTokenRedisRepository.existsInWaitingQueue(userId)) {
            String jwtToken = tokenProvider.issueToken(userId);
            return WaitingTokenIssueTokenDto.of(userId,TokenStatus.WAIT, jwtToken);
        }

        // 신규 진입
        waitingTokenRedisRepository.addToWaitingQueue(userId);
        String jwtToken = tokenProvider.issueToken(userId);
        return WaitingTokenIssueTokenDto.of(userId,TokenStatus.WAIT, jwtToken);
    }

    public WaitingOrderDto getWaitingOrder(Long userId) {
        log.info("WaitingTokenService getWaitingOrder(): userId={}", userId);

        if (waitingTokenRedisRepository.isValidActiveToken(userId)) {
            return WaitingOrderDto.active();
        }

        if (waitingTokenRedisRepository.existsInWaitingQueue(userId)) {
            Long order = waitingTokenRedisRepository.getWaitingOrder(userId);
            return order != null ? WaitingOrderDto.waiting(order + 1) : WaitingOrderDto.invalid();
        }

        return WaitingOrderDto.invalid();
    }
}
