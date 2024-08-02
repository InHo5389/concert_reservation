package concert.domain.token;

import concert.common.exception.BusinessException;
import concert.domain.common.RedisRepository;
import concert.domain.token.dto.WaitingOrderDto;
import concert.domain.token.dto.WaitingTokenIssueTokenDto;
import concert.domain.token.jwt.WaitingTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static concert.domain.token.TokenStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingTokenService {

    private final WaitingTokenRepository waitingTokenRepository;
    private final WaitingTokenProvider tokenProvider;
    private final RedisRepository redisRepository;

    private static final int EXPIRATION_MINUTES = 5;
    private static final String ACTIVE_QUEUE = "activequeue";
    private static final String WAITING_QUEUE = "waitingqueue";

    public void issueWaitingToken(Long userId) {
        log.info("WaitingTokenService issueToken(): userId={}",userId);
        redisRepository.zSetAdd(WAITING_QUEUE,String.valueOf(userId),System.currentTimeMillis());
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
