package concert.controller.concert.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AvailableConcertDateResponse {

    private LocalDateTime concertDate;
}
