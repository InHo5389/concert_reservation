package concert.domain.concert;

import concert.domain.concert.entity.Concert;
import concert.domain.concert.entity.ConcertSchedule;
import concert.domain.concert.entity.Seat;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

@Transactional
@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    @Test
    @DisplayName("예약 가능한 좌석수를 보여준다.")
    void availableSeatsCount(){
        //given
        Long concertId = 1L;
        LocalDateTime now = LocalDateTime.now();
        given(concertRepository.findById(concertId))
                .willReturn(Optional.of(new Concert(concertId,"임영웅 콘서트","임영웅")));
        ConcertSchedule schedule = new ConcertSchedule(1L, concertId, now);
        given(concertRepository.findByConcertIdAndConcertDateTime(concertId, now))
                .willReturn(Optional.of(schedule));

        Seat seat1 = Seat.builder().id(1L).concertScheduleId(1L).seatNumber(1).seatStatus(SeatStatus.AVAILABLE).seatPrice(2000).build();
        Seat seat2 = Seat.builder().id(1L).concertScheduleId(1L).seatNumber(2).seatStatus(SeatStatus.AVAILABLE).seatPrice(2000).build();
        Seat seat3 = Seat.builder().id(1L).concertScheduleId(1L).seatNumber(3).seatStatus(SeatStatus.RESERVED).seatPrice(2000).build();
        Seat seat4 = Seat.builder().id(1L).concertScheduleId(1L).seatNumber(4).seatStatus(SeatStatus.RESERVED).seatPrice(2000).build();
        given(concertRepository.findByConcertScheduleIdAndSeatStatus(schedule.getConcertId(), SeatStatus.AVAILABLE))
                .willReturn(List.of(seat1,seat2,seat3,seat4));
        //when
        //then
        Assertions.assertThat(concertService.availableSeats(concertId,now))
                .isEqualTo(List.of(seat1,seat2,seat3,seat4));
    }

}