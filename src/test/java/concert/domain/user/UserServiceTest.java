package concert.domain.user;

import concert.domain.user.dto.AmountChargeDto;
import concert.domain.user.entity.User;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("포인트를 충전하면 잔액 내역 테이블에서 잔액 상태는 충전이다.")
    void chargeAmount() {
        //given
        int chargeAmount = 300;
        int userAmount = 5500;
        User user = userRepository.save(User.builder().phone("01012345678").amount(userAmount).build());
        userRepository.save(user);
        //when
        AmountChargeDto amountHistory = userService.chargeAmount(user.getId(), chargeAmount);

        //then
        Assertions.assertThat(amountHistory).extracting("useAmount", "remainAmount", "status")
                .contains(chargeAmount, chargeAmount + userAmount, AmountStatus.CHARGE);
    }
}