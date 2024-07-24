package concert.controller.concert.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateConcertRequest {
    private String name;
    private String title;
    private LocalDateTime concertDateTime;
    private int baseSeatPrice;
}
