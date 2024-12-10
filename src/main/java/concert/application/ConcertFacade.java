package concert.application;

import concert.application.dto.CreateConcertDto;
import concert.domain.concert.entity.ConcertSchedule;
import concert.domain.concert.ConcertService;

import concert.domain.concert.entity.Seat;
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
