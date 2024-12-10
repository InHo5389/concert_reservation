package concert.api.concert.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableSeatRequest {

    private Long concertId;
    private LocalDateTime concertDate;
}
