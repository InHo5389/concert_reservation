package concert.domain.concert;

import concert.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    public List<ConcertSchedule> availableDates(Long concertId) {
        log.info("ConcertService availableDates(): concertId={}",concertId);
        Concert concert = getConcert(concertId);

        LocalDateTime now = LocalDateTime.now();
        return concertRepository.findByConcert(concert.getId()).stream()
                .filter(schedule -> schedule.isAvailableConcert(now))
                .collect(Collectors.toList());
    }

    public List<Seat> availableSeats(Long concertId, LocalDateTime concertDate) {
        log.info("ConcertService availableSeats(): concertId={}, concertDate={}",concertId,concertDate);
        Concert concert = getConcert(concertId);

        ConcertSchedule concertSchedule = concertRepository.findByConcertIdAndConcertDateTime(concert.getId(), concertDate)
                .orElseThrow(()->new BusinessException("해당 날짜에 해당하는 콘서트가 없습니다."));
        return concertRepository.findByConcertScheduleIdAndSeatStatus(concertSchedule.getConcertId(), SeatStatus.AVAILABLE);
    }

    public Concert getConcert(Long concertId) {
        log.info("ConcertService getConcert(): concertId={}",concertId);
        return concertRepository.findById(concertId)
                .orElseThrow(() -> new BusinessException("해당 콘서트가 없습니다."));
    }

    public Seat getSeat(Long seatId) {
        log.info("ConcertService getSeat(): seatId={}",seatId);
        return concertRepository.findBySeatId(seatId)
                .orElseThrow(() -> new BusinessException("좌석이 존재하지 않습니다."));
    }
}
