package concert.domain.concert;

import concert.common.exception.BusinessException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @Version
    private Long version;

    public void seatStatusReserved(){
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
