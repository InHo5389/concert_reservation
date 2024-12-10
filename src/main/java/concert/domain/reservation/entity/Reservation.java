package concert.domain.reservation.entity;

import concert.common.exception.BusinessException;
import concert.domain.reservation.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@Slf4j
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long seatId;
    private Long userId;
    private LocalDateTime concertDate;
    private int reservationAmount;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime expirationTime;

    public static Reservation createReservation(Long seatId,LocalDateTime concertDate,int expireMinutes,Long userId,int reservationAmount){
        log.info("Reservation create");

        LocalDateTime now = LocalDateTime.now();
        return Reservation.builder()
                .seatId(seatId)
                .userId(userId)
                .concertDate(concertDate)
                .status(ReservationStatus.RESERVED)
                .reservationAmount(reservationAmount)
                .createdAt(now)
                .modifiedAt(now)
                .expirationTime(now.plusMinutes(expireMinutes))
                .build();
    }

    public void validateAndComplete() {
        log.info("Reservation validateAndComplete(): id={}", this.id);

        if (this.status != ReservationStatus.RESERVED) {
            log.warn("Invalid reservation status: id={}, status={}", this.id, this.status);
            throw new BusinessException("유효하지 않은 예약입니다.");
        }

        if (isExpired()) {
            log.warn("Expired reservation: id={}, expirationTime={}", this.id, this.expirationTime);
            throw new BusinessException("예약 시간이 만료되었습니다.");
        }

        this.status = ReservationStatus.PAID;
        this.modifiedAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.getExpirationTime());
    }

}
