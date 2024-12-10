package concert.api.concert;

import concert.application.ConcertFacade;
import concert.api.concert.request.AvailableSeatRequest;

import concert.api.concert.request.CreateConcertRequest;
import concert.api.concert.response.AvailableConcertDateResponse;
import concert.api.concert.response.CreateConcertResponse;

import concert.domain.concert.entity.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertFacade concertFacade;

    @GetMapping("/seats")
    public List<Seat> getAvailableSeat(@RequestBody AvailableSeatRequest request){
        return concertFacade.availableSeats(request.getConcertId(), request.getConcertDate());
    }

    @GetMapping("/dates/{concertId}")
    public List<AvailableConcertDateResponse> getAvailableDate(@PathVariable Long concertId) {
        return AvailableConcertDateResponse.of(concertFacade.availableDates(concertId));
    }

    @PostMapping
    public CreateConcertResponse createConcert(@RequestBody CreateConcertRequest request) {
        return CreateConcertResponse.of(concertFacade.createConcert(request.getName(), request.getTitle(),request.getConcertDateTime(), request.getBaseSeatPrice()));
    }
}
