package concert.controller.concert.response;

import concert.domain.concert.CreateConcertDto;
import concert.domain.concert.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateConcertResponse {

    private Long concertId;
    private String title;
    private String name;
    private Long concertScheduleId;
    private LocalDateTime concertDateTime;
    private List<Seat> seats;

    public static CreateConcertResponse of(CreateConcertDto dto){
        return CreateConcertResponse.builder()
                .concertId(dto.getConcertId())
                .title(dto.getTitle())
                .name(dto.getName())
                .concertScheduleId(dto.getConcertScheduleId())
                .concertDateTime(dto.getConcertDateTime())
                .seats(dto.getSeats())
                .build();
    }
}
