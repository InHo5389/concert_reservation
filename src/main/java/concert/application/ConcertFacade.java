package concert.application;

import concert.domain.concert.ConcertSchedule;
import concert.domain.concert.ConcertService;
import concert.domain.concert.Seat;
import concert.domain.token.jwt.WaitingTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;
    private final WaitingTokenValidator waitingTokenValidator;

    public List<ConcertSchedule> availableDates(Long concertId,String jwtToken){
        waitingTokenValidator.isTokenActive(jwtToken);
        return concertService.availableDates(concertId);
    }

    public List<Seat> availableSeats(Long concertId, LocalDateTime concertDate, String jwtToken){
        waitingTokenValidator.isTokenActive(jwtToken);
        return concertService.availableSeats(concertId,concertDate);
    }
}
