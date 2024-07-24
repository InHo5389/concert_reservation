package concert.controller.concert.response;

import concert.domain.concert.ConcertSchedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class AvailableConcertDateResponse {

    private LocalDateTime concertDate;

    public static List<AvailableConcertDateResponse> of(List<ConcertSchedule> concertScheduleList) {
        return concertScheduleList.stream()
                .map(schedule -> new AvailableConcertDateResponse(schedule.getConcertDateTime()))
                .collect(Collectors.toList());
    }
}
