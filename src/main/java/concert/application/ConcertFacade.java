package concert.application;

import concert.domain.concert.ConcertSchedule;
import concert.domain.concert.ConcertService;
import concert.domain.concert.CreateConcertDto;

import concert.domain.concert.Seat;
import concert.domain.token.WaitingTokenService;
import concert.domain.token.jwt.WaitingTokenValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;

    public List<ConcertSchedule> availableDates(Long concertId) {
        log.info("ConcertFacade availableDates(): concertId={}", concertId);
        return concertService.availableDates(concertId);
    }

    public List<Seat> availableSeats(Long concertId, LocalDateTime concertDate) {
        log.info("ConcertFacade availableSeats(): concertId={} ,concertDate={}", concertId, concertDate);
        return concertService.availableSeats(concertId, concertDate);
    }

    public CreateConcertDto createConcert(String name, String title, LocalDateTime concertDateTime, int baseSeatPrice) {
        return concertService.createConcert(name, title, concertDateTime, baseSeatPrice);
    }
}
