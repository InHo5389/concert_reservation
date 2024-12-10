package concert.infrastructure.concert;

import concert.domain.concert.ConcertRepository;
import concert.domain.concert.entity.Seat;
import concert.domain.concert.SeatStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
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
        seatJpaRepository.save(Seat.builder().id(1L).concertScheduleId(1L).seatNumber(1).seatStatus(SeatStatus.AVAILABLE).seatPrice(2000).build());
        seatJpaRepository.save(Seat.builder().id(2L).concertScheduleId(1L).seatNumber(2).seatStatus(SeatStatus.AVAILABLE).seatPrice(2000).build());
        seatJpaRepository.save(Seat.builder().id(3L).concertScheduleId(1L).seatNumber(3).seatStatus(SeatStatus.RESERVED).seatPrice(2000).build());
        seatJpaRepository.save(Seat.builder().id(4L).concertScheduleId(1L).seatNumber(4).seatStatus(SeatStatus.RESERVED).seatPrice(2000).build());
        seatJpaRepository.save(Seat.builder().id(5L).concertScheduleId(1L).seatNumber(5).seatStatus(SeatStatus.RESERVED).seatPrice(2000).build());
        //when
        //then
        List<Seat> seatList = concertRepository.findByConcertScheduleIdAndSeatStatus(1L, SeatStatus.AVAILABLE);
        Assertions.assertThat(seatList).hasSize(2);
    }

}