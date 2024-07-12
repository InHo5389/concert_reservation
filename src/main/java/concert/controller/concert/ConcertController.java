package concert.controller.concert;

import concert.application.ConcertFacade;
import concert.controller.concert.request.AvailableSeatRequest;
import concert.controller.concert.response.AvailableConcertDateResponse;
import concert.controller.concert.response.AvailableSeatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertFacade concertFacade;

    @GetMapping("/available-seats")
    public List<AvailableSeatResponse> getAvailableSeat(@RequestBody AvailableSeatRequest request){
        AvailableSeatResponse response1 = new AvailableSeatResponse(1,10000,"EMPTY");
        AvailableSeatResponse response2 = new AvailableSeatResponse(2,10000,"RESERVED");
        AvailableSeatResponse response3 = new AvailableSeatResponse(3,10000,"SORTED");
        return List.of(response1,response2,response3);
    }

    @GetMapping(value = "/available-dates", headers = "Authorization" )
    public List<AvailableConcertDateResponse> getAvailableDate(@RequestHeader(name = "Authorization") String jwtToken,Long concertId){
        return AvailableConcertDateResponse.of(concertFacade.availableDates(concertId,jwtToken));
    }
}
