package concert.domain.concert;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CreateConcertDto {

    private Long concertId;
    private String title;
    private String name;
    private Long concertScheduleId;
    private LocalDateTime concertDateTime;
    private List<Seat> seats;
}
