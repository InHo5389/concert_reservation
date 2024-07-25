package concert.domain.reservation;

import concert.common.exception.BusinessException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ReservationServiceConcurrencyTest {

    @Autowired
    private ReservationService reservationService;

    /**
     * 복합 키를 통한 동시성 제어
     */
    @Test
    @DisplayName("userId가 다른 10명이 같은 좌석을 예약할 때 1명만 좌석을 예약할 수 있다.")
    void testReservationConcurrencyWithCompositeKey() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        System.out.println("Execution time with Composite Key Test Start "+startTime+"ms");

        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successfulReservations = new AtomicInteger(0);
        AtomicInteger failedReservations = new AtomicInteger(0);

        LocalDateTime concertDate = LocalDateTime.now().plusDays(7);
        Long seatId = 1L;

        for (int i = 0; i < numberOfThreads; i++) {
            Long userId = (long) i;
            executorService.submit(() -> {
                try {
                    Reservation reservation = reservationService.reserveSeat(concertDate, seatId, userId, 100);
                    assertNotNull(reservation);
                    successfulReservations.incrementAndGet();
                } catch (BusinessException e) {
                    failedReservations.incrementAndGet();
                } catch (Exception e) {
                    fail("Unexpected exception: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time with Composite Key: " + executionTime + "ms");

        Assertions.assertThat(1).isEqualTo(successfulReservations.get());
        Assertions.assertThat(numberOfThreads - 1).isEqualTo(failedReservations.get());
    }
}