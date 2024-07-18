package concert.domain.reservation;

import concert.common.exception.BusinessException;
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
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"concertDate", "seatId"})
})
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long seatId;
    private Long userId;
    private LocalDateTime concertDate;
    private int reservationAmount;

    @Setter
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime expirationTime;

    public static Reservation createReservation(Long seatId,LocalDateTime concertDate,int expireMinutes){
        log.info("Reservation create");

        LocalDateTime now = LocalDateTime.now();
        return Reservation.builder()
                .seatId(seatId)
                .concertDate(concertDate)
                .status(ReservationStatus.RESERVED)
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
