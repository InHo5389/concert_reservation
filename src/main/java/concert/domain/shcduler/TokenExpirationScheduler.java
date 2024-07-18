package concert.domain.shcduler;

import concert.domain.reservation.Reservation;
import concert.domain.reservation.ReservationRepository;
import concert.domain.reservation.ReservationStatus;
import concert.domain.token.TokenStatus;
import concert.domain.token.WaitingToken;
import concert.domain.token.WaitingTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenExpirationScheduler {

    private final WaitingTokenRepository waitingTokenRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 만료된 토큰 상태를 EXPIRED 변경하는 스케줄러
     */
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    @Transactional
    public void expireTokens() {
        LocalDateTime now = LocalDateTime.now();
        List<WaitingToken> expiredTokens = waitingTokenRepository.findByExpiredAtBeforeAndTokenStatus(now, TokenStatus.ACTIVE);

        for (WaitingToken token : expiredTokens) {
            token.expire();
            log.info("Token expired: {}", token.getId());
        }

        waitingTokenRepository.saveAll(expiredTokens);
        log.info("Expired {} tokens", expiredTokens.size());
    }

    /**
     * WAIT토큰을 대기열 입장 개수만큼 맞춰서 ACTIVE토큰을 바꾸어주는 스케줄러
     */
    @Scheduled(fixedRate = 30000) // 30초마다 실행
    @Transactional
    public void manageActiveTokens() {
        int activeTokenCount = waitingTokenRepository.countByTokenStatus(TokenStatus.ACTIVE);
        log.info("Current active token count: {}", activeTokenCount);

        if (activeTokenCount < 50) {
            int tokensToActivate = 50 - activeTokenCount;
            PageRequest pageRequest = PageRequest.of(0, tokensToActivate);
            List<WaitingToken> waitingTokens = waitingTokenRepository.findByTokenStatusOrderByCreatedAt(TokenStatus.WAIT, pageRequest);

            for (WaitingToken token : waitingTokens) {
                token.activate();
                log.info("Activated token: {}", token.getId());
            }

            waitingTokenRepository.saveAll(waitingTokens);
            log.info("Activated {} tokens", waitingTokens.size());
        }
    }

    /**
     * 콘서트를 예약하고 5분동안 처리가 되지 않으면 삭제해주는 스케줄러
     */
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    @Transactional
    public void cleanupExpiredReservations() {
        List<Reservation> expiredReservations = reservationRepository
                .findByStatusAndExpirationTimeBefore(ReservationStatus.RESERVED, LocalDateTime.now());

        for (Reservation reservation : expiredReservations) {
            reservationRepository.delete(reservation);
            log.info("Deleted token: {}",reservation.getId());
        }
    }
}
