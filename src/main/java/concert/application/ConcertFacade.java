package concert.application;

import concert.domain.concert.ConcertSchedule;
import concert.domain.concert.ConcertService;
import concert.domain.token.jwt.WaitingTokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
}
