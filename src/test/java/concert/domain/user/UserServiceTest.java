package concert.domain.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @Test
    @DisplayName("포인트를 충전하면 잔액 내역 테이블에서 잔액 상태는 충전이다.")
    void chargeAmount() {
        //given
        long userId = 1L;
        int chargeAmount = 300;
        int userAmount = 5500;
        User user = new User(userId, "1@naver.com", "1234", "이노", "01012345678", userAmount);
        userRepository.save(user);
        //when
        AmountChargeDto amountHistory = userService.chargeAmount(userId, chargeAmount);
        //then
        Assertions.assertThat(amountHistory.amountHistory).extracting("useAmount", "remainAmount", "status")
                .contains(chargeAmount, chargeAmount + userAmount, AmountStatus.CHARGE);
    }
}