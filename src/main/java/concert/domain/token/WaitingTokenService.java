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

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingTokenService {

    private final WaitingTokenRepository waitingTokenRepository;
    private final WaitingTokenProvider tokenProvider;

    public WaitingTokenIssueTokenDto issueToken(Long userId) {
        log.info("WaitingTokenService issueToken(): userId={}", userId);

        // ACTIVE나 WAIT 상태의 토큰 조회
        Optional<WaitingToken> activeOrWaitToken = waitingTokenRepository
                .findFirstByUserIdAndTokenStatusInOrderByCreatedAtDesc(
                        userId,
                        Arrays.asList(TokenStatus.ACTIVE, TokenStatus.WAIT)
                );

        if (activeOrWaitToken.isPresent()) {
            WaitingToken existingToken = activeOrWaitToken.get();
            existingToken.expire();
            waitingTokenRepository.save(existingToken);
        }

        // 신규 토큰 발급
        int activeCount = waitingTokenRepository.countByTokenStatus(TokenStatus.ACTIVE);
        WaitingToken newToken = WaitingToken.issue(
                userId,
                activeCount,
                TokenUtil.FIX_ACTIVE_COUNT,
                TokenUtil.EXPIRATION_MINUTES
        );

        WaitingToken savedToken = waitingTokenRepository.save(newToken);
        String jwtToken = tokenProvider.issueToken(savedToken);

        return WaitingTokenIssueTokenDto.from(savedToken, jwtToken);
    }

    public WaitingOrderDto getWaitingOrder(Long userId) {
        log.info("WaitingTokenService verifyAndGetWaitingOrder(): userId={}", userId);
        return calculateWaitingOrder(userId);
    }

    private WaitingOrderDto calculateWaitingOrder(Long userId) {
        WaitingToken waitingToken = waitingTokenRepository
                .findFirstByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new BusinessException("토큰이 존재하지 않습니다."));

        if (waitingToken.getTokenStatus().equals(TokenStatus.ACTIVE)) {
            log.info("WaitingToken Active");
            return WaitingOrderDto.active();
        } else if (waitingToken.getTokenStatus().equals(TokenStatus.EXPIRED)) {
            log.info("WaitingToken Invalid");
            return WaitingOrderDto.invalid();
        }

        long myWaitingOrder = waitingTokenRepository
                .countByTokenStatusAndCreatedAtLessThanEqual(
                        TokenStatus.WAIT,
                        waitingToken.getCreatedAt()
                );

        return WaitingOrderDto.waiting(myWaitingOrder);
    }
}
