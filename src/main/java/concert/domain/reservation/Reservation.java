package concert.domain.reservation;

import concert.common.exception.BusinessException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    public void validateAndComplete() {
        if (this.status != ReservationStatus.RESERVED) {
            throw new BusinessException("유효하지 않은 예약입니다.");
        }

        if (isExpired()) {
            throw new BusinessException("예약 시간이 만료되었습니다.");
        }

        this.status = ReservationStatus.PAID;
        this.modifiedAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.getExpirationTime());
    }

}
