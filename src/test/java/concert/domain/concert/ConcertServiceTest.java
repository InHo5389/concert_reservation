package concert.domain.concert;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

        Seat seat1 = new Seat(1L, 1L, 1, SeatStatus.AVAILABLE,2000);
        Seat seat2 = new Seat(1L, 1L, 2, SeatStatus.AVAILABLE,2000);
        Seat seat3 = new Seat(1L, 1L, 3, SeatStatus.RESERVED,2000);
        Seat seat4 = new Seat(1L, 1L, 4, SeatStatus.RESERVED,2000);
        given(concertRepository.findByConcertScheduleIdAndSeatStatus(schedule.getConcertId(), SeatStatus.AVAILABLE))
                .willReturn(List.of(seat1,seat2,seat3,seat4));
        //when
        //then
        Assertions.assertThat(concertService.availableSeats(concertId,now))
                .isEqualTo(List.of(seat1,seat2,seat3,seat4));
    }

}