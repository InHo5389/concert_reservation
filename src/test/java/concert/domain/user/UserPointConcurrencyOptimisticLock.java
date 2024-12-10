package concert.domain.user;

import concert.application.UserFacade;
import concert.domain.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@ActiveProfiles("test")
@SpringBootTest
public class UserPointConcurrencyOptimisticLock {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFacade userFacade;

    private static final int numberOfThreads = 1000;

    @BeforeEach
    void setUp() {
        userRepository.save(User.builder().amount(0).username("정인호").build());
    }

    @Test
    @DisplayName("한 유저가 0원을 가지고 있고 포인트를 1000원씩 1000번 충전할때 1000000원이 나와야 한다.")
    void UserPointConcurrencyOptimisticLock() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        System.out.println("Execution time with Composite Key Test Start " + startTime + "ms");
        AtomicInteger failedReservations = new AtomicInteger(0);

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        Long userId = 1L;

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    userFacade.chargeAmount(userId,1000);
                }catch (OptimisticLockingFailureException e){
                    failedReservations.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        int failCount = failedReservations.get();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time with Optimistic Lock: " + executionTime + "ms");
        User user = userRepository.findById(userId).get();
        Assertions.assertThat(user.getAmount()).isEqualTo(1000000 - (failCount * 1000));

    }
}
