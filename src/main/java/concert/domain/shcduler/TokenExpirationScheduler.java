package concert.domain.shcduler;

import concert.common.exception.BusinessException;
import concert.domain.concert.ConcertRepository;
import concert.domain.concert.entity.Seat;
import concert.domain.reservation.ReservationRepository;
import concert.domain.reservation.ReservationStatus;
import concert.domain.reservation.entity.Reservation;
import concert.domain.token.WaitingTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenExpirationScheduler {

    private static final int PROMOTION_SIZE = 50;  // 20초마다 50명씩 처리

    private final WaitingTokenRedisRepository waitingTokenRedisRepository;
    private final ReservationRepository reservationRepository;
    private final ConcertRepository concertRepository;

    /**
     * redis - wait대기열을 20초마다 50명 active 대기열로 넘겨주는 스케줄러
     */
    @Scheduled(fixedRate = 20000) // 20초마다 실행
    public void promoteWaitingToActive() {
        log.info("Promoting waiting tokens to active...");

        Set<String> waitingTokens = waitingTokenRedisRepository.getWaitingTokens(PROMOTION_SIZE);
        if (waitingTokens.isEmpty()) {
            return;
        }

        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);
        for (String userId : waitingTokens) {
            waitingTokenRedisRepository.addToActiveQueue(Long.parseLong(userId), expiredAt);
        }

        waitingTokenRedisRepository.removeFromWaitingQueue(waitingTokens);
    }

    /**
     * 대기열을 나간 사용자들을 정리하는 스케줄러
     */
    @Scheduled(cron = "0 50 23 * * ?")
    public void cleanupAbandonedActiveQueue() {
        log.info("Cleaning up abandoned active queue members");

        Set<String> activeUsers = waitingTokenRedisRepository.getAllActiveUsers();

        for (String userId : activeUsers) {
            waitingTokenRedisRepository.removeFromActiveQueue(Long.parseLong(userId));
            log.info("Removed abandoned user from waiting queue: {}", userId);
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
            Seat seat = concertRepository.findBySeatId(reservation.getSeatId())
                    .orElseThrow(() -> new BusinessException("해당 좌석이 없습니다."));
            seat.seatStatusAvailable();
            reservationRepository.delete(reservation);
            log.info("Deleted reservation: {}", reservation.getId());
        }
    }
}
