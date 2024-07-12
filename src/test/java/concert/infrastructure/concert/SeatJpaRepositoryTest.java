package concert.infrastructure.concert;

import concert.domain.concert.Concert;
import concert.domain.concert.ConcertRepository;
import concert.domain.concert.Seat;
import concert.domain.concert.SeatStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;



@SpringBootTest
class SeatJpaRepositoryTest {

    @Autowired
    private SeatJpaRepository seatJpaRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Test
    @DisplayName("이용가능한 좌석들을 보여준다.")
    void availableSeatsCount(){
        //given
        seatJpaRepository.save(new Seat(1L,1L,1, SeatStatus.AVAILABLE,2000));
        seatJpaRepository.save(new Seat(2L,1L,2, SeatStatus.AVAILABLE,2000));
        seatJpaRepository.save(new Seat(3L,1L,3, SeatStatus.RESERVED,2000));
        seatJpaRepository.save(new Seat(4L,1L,4, SeatStatus.RESERVED,2000));
        seatJpaRepository.save(new Seat(5L,1L,5, SeatStatus.RESERVED,2000));
        //when
        //then
        List<Seat> seatList = concertRepository.findByConcertScheduleIdAndSeatStatus(1L, SeatStatus.AVAILABLE);
        Assertions.assertThat(seatList).hasSize(2);
    }

}