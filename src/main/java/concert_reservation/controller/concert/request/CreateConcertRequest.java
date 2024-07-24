package concert.controller.concert.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateConcertRequest {
    private String name;
    private String title;
    private LocalDateTime concertDateTime;
    private int baseSeatPrice;
}
