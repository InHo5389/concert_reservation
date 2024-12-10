package concert.domain.concert.entity;

import concert.common.exception.BusinessException;
import concert.domain.concert.SeatStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long concertScheduleId;
    private int seatNumber;
    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus;
    private int seatPrice;

    public void seatStatusReserved(){
        if (this.seatStatus != SeatStatus.AVAILABLE) {
            throw new BusinessException("이미 예약된 좌석입니다.");
        }
        this.seatStatus = SeatStatus.RESERVED;
    }
    public void seatStatusAvailable(){
        this.seatStatus = SeatStatus.AVAILABLE;
    }

    public static Seat create(Long concertScheduleId,int seatNumber,int seatPrice){
        return Seat.builder()
                .concertScheduleId(concertScheduleId)
                .seatNumber(seatNumber)
                .seatStatus(SeatStatus.AVAILABLE)
                .seatPrice(seatPrice)
                .build();
    }

}
