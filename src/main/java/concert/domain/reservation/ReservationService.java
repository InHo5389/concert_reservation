package concert.domain.reservation;

import concert.common.exception.BusinessException;
import concert.domain.concert.ConcertRepository;
import concert.domain.concert.Seat;
import concert.domain.user.AmountHistory;
import concert.domain.user.AmountStatus;
import concert.domain.user.User;
import concert.domain.user.UserRepository;
import concert.infrastructure.reservation.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ConcertRepository concertRepository;

    private static final int FIXED_EXPIRED_MINUTES = 5;

    @Transactional
    public Reservation reserveSeat(LocalDateTime concertDate, Long seatId) {

        Reservation existingReservation = reservationRepository.findByConcertDateAndSeatIdAndStatus(concertDate, seatId, ReservationStatus.RESERVED);
        if (existingReservation != null) {
            throw new BusinessException("좌석이 이미 예약되었습니다.");
        }

        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = Reservation.builder()
                .seatId(seatId)
                .concertDate(concertDate)
                .status(ReservationStatus.RESERVED)
                .createdAt(now)
                .modifiedAt(now)
                .expirationTime(now.plusMinutes(FIXED_EXPIRED_MINUTES))
                .build();

        try {
            return reservationRepository.save(reservation);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("동시에 같은 좌석을 예약하려는 시도가 있었습니다.");
        }
    }

    @Transactional
    public PaymentDto pay(Long reservationId, Long userId, int amount) {
        // 1. 결제 처리 로직
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("회원을 찾을수 없습니다."));
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException("예약된 좌석이 없습니다."));
        Seat seat = concertRepository.findBySeatId(reservation.getSeatId())
                .orElseThrow(() -> new BusinessException("좌석이 존재하지 않습니다."));
        int price = reservation.getReservationAmount();
        int userAmount = user.getAmount();
        LocalDateTime now = LocalDateTime.now();
        userAmount -= price;
        if (reservation.getStatus() != ReservationStatus.RESERVED) {
            throw new BusinessException("유효하지 않은 예약입니다.");
        }

        if (now.isAfter(reservation.getExpirationTime())) {
            throw new BusinessException("예약 시간이 만료되었습니다.");
        }

        if (!user.availablePay(amount)) {
            throw new BusinessException("잔액이 부족합니다.");
        }
        user.decreaseAmount(amount);

        reservation.setStatus(ReservationStatus.PAID);
        AmountHistory amountHistory = userRepository.save(AmountHistory.builder()
                .userId(userId)
                .useAmount(price)
                .remainAmount(userAmount)
                .status(AmountStatus.USE)
                .build());
        userRepository.save(amountHistory);

        reservationRepository.save(Payment.builder()
                        .reservationId(reservationId)
                        .paymentAmount(price)
                        .status(PaymentStatus.COMPLETED)
                        .createdAt(now)
                        .modifiedAt(now)
                .build());
        return PaymentDto.builder()
                .username(user.getUsername())
                .useAmount(price)
                .seatNumber(seat.getSeatNumber())
                .seatPrice(seat.getSeatPrice())
                .createAt(now)
                .build();
    }
}
