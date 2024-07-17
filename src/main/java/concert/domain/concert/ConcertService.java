package concert.domain.concert;

import concert.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    public List<ConcertSchedule> availableDates(Long concertId){
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new BusinessException("해당 콘서트가 없습니다."));

        LocalDateTime now = LocalDateTime.now();
        return concertRepository.findByConcert(concert).stream()
                .filter(schedule->schedule.isAvailableConcert(now))
                .collect(Collectors.toList());
    }

    public List<Seat> availableSeats(Long concertId, LocalDateTime concertDate){
        concertRepository.findById(concertId)
                .orElseThrow(() -> new BusinessException("해당 콘서트가 없습니다."));

        ConcertSchedule concertSchedule = concertRepository.findByConcertIdAndConcertDateTime(concertId, concertDate);
        return concertRepository.findByConcertScheduleIdAndSeatStatus(concertSchedule.getConcertId(), SeatStatus.AVAILABLE);
    }

    public Optional<Seat> getSeat(Long seatId){
        return concertRepository.findBySeatId(seatId);
    }
}
