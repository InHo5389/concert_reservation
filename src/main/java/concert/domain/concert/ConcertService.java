package concert.domain.concert;

import concert.application.dto.CreateConcertDto;
import concert.common.exception.BusinessException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    private static final int SEAT_COUNT = 50;
    private static final int PRICE_DECREMENT = 5000;
    private static final int SEAT_PER_PRICE_LEVEL = 10;


    public List<ConcertSchedule> availableDates(Long concertId) {
        log.info("ConcertService availableDates(): concertId={}", concertId);

        Concert concert = getConcert(concertId);

        LocalDateTime now = LocalDateTime.now();
        return concertRepository.findByConcert(concert.getId()).stream()
                .filter(schedule -> schedule.isAvailableConcert(now))
                .collect(Collectors.toList());
    }

    public List<Seat> availableSeats(Long concertId, LocalDateTime concertDate) {
        log.info("ConcertService availableSeats(): concertId={}, concertDate={}", concertId, concertDate);
        Concert concert = getConcert(concertId);

        ConcertSchedule concertSchedule = concertRepository.findByConcertIdAndConcertDateTime(concert.getId(), concertDate)
                .orElseThrow(() -> new BusinessException("해당 날짜에 해당하는 콘서트가 없습니다."));

        return concertRepository.findByConcertScheduleIdAndSeatStatus(concertSchedule.getConcertId(), SeatStatus.AVAILABLE);
    }

    public Concert getConcert(Long concertId) {
        log.info("ConcertService getConcert(): concertId={}", concertId);

        return concertRepository.findById(concertId)
                .orElseThrow(() -> new BusinessException("해당 콘서트가 없습니다."));
    }

    public Seat getSeat(Long seatId) {

        log.info("ConcertService getSeat(): seatId={}", seatId);
        return concertRepository.findBySeatId(seatId)
                .orElseThrow(() -> new BusinessException("좌석이 존재하지 않습니다."));
    }

    public Seat getSeatByOptimisticLock(Long seatId) {
        log.info("ConcertService getSeatByOptimisticLock(): seatId={}", seatId);

        return concertRepository.findByIdOptimisticLock(seatId)
                .orElseThrow(() -> new BusinessException("좌석이 존재하지 않습니다."));
    }

    public ConcertSchedule getConcertSchedule(Long concertScheduleId) {
        return concertRepository.findScheduleById(concertScheduleId)
                .orElseThrow(() -> new BusinessException("콘서트 스케줄이 존재하지 않습니다."));
    }

    @Transactional
    public CreateConcertDto createConcert(String name, String title, LocalDateTime concertDateTime, int baseSeatPrice) {
        Concert concert = concertRepository.save(Concert.create(title, name));
        ConcertSchedule concertSchedule = concertRepository.save(ConcertSchedule.create(concert.getId(), concertDateTime));


        List<Seat> seats = createSeats(concertSchedule.getId(), baseSeatPrice);

        List<Seat> savedSeats = concertRepository.saveAll(seats);

        return new CreateConcertDto(concert.getId(), concert.getTitle(), concert.getName(), concertSchedule.getId(), concertSchedule.getConcertDateTime(), savedSeats);
    }

    public List<Seat> createSeats(Long concertScheduleId, int baseSeatPrice) {
        return IntStream.range(0, SEAT_COUNT)
                .mapToObj(i -> createSeat(concertScheduleId, i + 1, baseSeatPrice))
                .collect(Collectors.toList());
    }

    private Seat createSeat(Long concertScheduleId, int seatNumber, int baseSeatPrice) {
        int seatPrice = calculateSeatPrice(seatNumber, baseSeatPrice);
        return Seat.create(concertScheduleId, seatNumber, seatPrice);
    }

    /**
     * 1~10 좌석 기본가격
     * 11~20 5000원씩 감소
     */
    private int calculateSeatPrice(int seatNumber, int baseSeatPrice) {
        int priceLevel = (seatNumber - 1) / SEAT_PER_PRICE_LEVEL;
        return baseSeatPrice - (priceLevel * PRICE_DECREMENT);
    }

}
