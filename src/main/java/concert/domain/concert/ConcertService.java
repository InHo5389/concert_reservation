package concert.domain.concert;

import concert.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    public List<ConcertSchedule> availableDates(Long concertId){
        concertRepository.findById(concertId)
                .orElseThrow(() -> new BusinessException("해당 콘서트가 없습니다."));

        LocalDateTime now = LocalDateTime.now();
        List<ConcertSchedule> concertScheduleList = concertRepository.findAll();
        return concertScheduleList.stream().filter(schedule -> schedule.getConcertId().equals(concertId) && schedule.availableConcert(now))
                .collect(Collectors.toList());
    }

    public List<Seat> availableSeats(Long concertId, LocalDateTime concertDate){
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new BusinessException("해당 콘서트가 없습니다."));

        ConcertSchedule concertSchedule = concertRepository.findByConcertIdAndConcertDateTime(concertId, concertDate);
        return concertRepository.findByConcertScheduleIdAndSeatStatus(concertSchedule.getConcertId(), SeatStatus.AVAILABLE);
    }
}
