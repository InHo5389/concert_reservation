package concert.domain.reservation;

import concert.application.ReservationFacade;
import concert.domain.concert.*;
import concert.domain.concert.entity.Concert;
import concert.domain.concert.entity.ConcertSchedule;
import concert.domain.concert.entity.Seat;
import concert.domain.user.entity.User;
import concert.domain.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@ActiveProfiles("test")
@SpringBootTest
class ReservationConcurrencyPessimisticTest {

    @Autowired
    private ReservationFacade reservationFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private static final int numberOfThreads = 1000;

    @BeforeEach
    void setUp() {
        for (int i = 1; i <= numberOfThreads; i++) {
            userRepository.save(User.builder().amount(1000000).username("정인호" + i).build());
        }
        long concertId = 1L;
        long concertScheduleId = 1L;
        long seatId = 1L;
        concertRepository.save(new Concert(concertId, "임영웅 콘서트", "임영웅"));
        concertRepository.save(new ConcertSchedule(concertScheduleId, concertId, LocalDateTime.now().plusDays(5)));
        concertRepository.save(Seat.builder()
                .id(seatId)
                .concertScheduleId(concertScheduleId)
                .seatNumber(1)
                .seatStatus(SeatStatus.AVAILABLE)
                .seatPrice(100)
                .build());
    }

    /**
     * 비관적 락을 통한 동시성 제어
     */
    @Test
    @DisplayName("userId가 다른 10명이 같은 좌석을 예약할 때 1명만 좌석을 예약할 수 있다.")
    void reservationConcurrencyWithOptimistic() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        System.out.println("Execution time with Composite Key Test Start " + startTime + "ms");

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successfulReservations = new AtomicInteger(0);
        AtomicInteger failedReservations = new AtomicInteger(0);

        LocalDateTime concertDate = LocalDateTime.now().plusDays(7);
        Long seatId = 1L;

        for (int i = 0; i < numberOfThreads; i++) {
            Long userId = (long) i + 1;
            executorService.submit(() -> {
                try {
                    reservationFacade.reserveSeat(concertDate, seatId, userId);
                    successfulReservations.incrementAndGet();
                } catch (Exception e) {
                    System.out.println("예외 발생: " + e.getClass().getSimpleName() + " - " + e.getMessage());
                    failedReservations.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time with Optimistic: " + executionTime + "ms");
        System.out.println("successfulReservations.get()" + successfulReservations.get());
        System.out.println("failedReservations.get()" + failedReservations.get());
        Assertions.assertThat(1).isEqualTo(successfulReservations.get());
        Assertions.assertThat(numberOfThreads - 1).isEqualTo(failedReservations.get());
    }
}