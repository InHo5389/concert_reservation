package concert.domain.reservation;

import concert.common.exception.BusinessException;
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

    @Test
    @DisplayName("1개의 좌석을 동시에 10개 스레드가 같이 요청하면 1개만 예약되어야 한다.")
    void ReservationServiceConcurrencyTest() throws InterruptedException {
        int numberOfThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successfulReservations = new AtomicInteger(0);
        AtomicInteger failedReservations = new AtomicInteger(0);
        List<Exception> exceptions = new ArrayList<>();

        LocalDateTime concertDate = LocalDateTime.now().plusDays(7);
        Long seatId = 1L;

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    Reservation reservation = reservationService.reserveSeat(concertDate, seatId);
                    assertNotNull(reservation);
                    successfulReservations.incrementAndGet();
                } catch (BusinessException e) {
                    failedReservations.incrementAndGet();
                    exceptions.add(e);
                } catch (Exception e) {
                    fail("Unexpected exception: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 작업을 마칠 때까지 대기
        executorService.shutdown();

        // 검증
        assertEquals(1, successfulReservations.get());
        assertEquals(numberOfThreads - 1, failedReservations.get());
    }

}