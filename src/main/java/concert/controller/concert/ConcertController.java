package concert.controller.concert;

import concert.application.ConcertFacade;
import concert.controller.concert.request.AvailableSeatRequest;
import concert.controller.concert.response.AvailableConcertDateResponse;
import concert.domain.concert.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertFacade concertFacade;

    @GetMapping(value = "/available-seats", headers = "Authorization" )
    public List<Seat> getAvailableSeat(@RequestHeader(name = "Authorization") String jwtToken, @RequestBody AvailableSeatRequest request){
        return concertFacade.availableSeats(request.getConcertId(), request.getConcertDate(),jwtToken);
    }

    @GetMapping(value = "/available-dates", headers = "Authorization" )
    public List<AvailableConcertDateResponse> getAvailableDate(@RequestHeader(name = "Authorization") String jwtToken,Long concertId){
        return AvailableConcertDateResponse.of(concertFacade.availableDates(concertId,jwtToken));
    }
}
