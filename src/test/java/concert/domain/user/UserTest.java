package concert.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    @DisplayName("유저 금액 조회를 한다.")
    void getAmount(){
        //given
        int userAmount = 5000;
        User user = new User(1L, "1234@naver.com", "1234", "이노", "01012345678", userAmount);
        //when
        int amount = user.getAmount();
        //then
        assertThat(amount).isEqualTo(userAmount);
    }

    @Test
    @DisplayName("유저 금액을 충전 한다.")
    void chargeAmount(){
        //given
        long chargeAmount = 2000;
        int userAmount = 5000;
        User user = new User(1L, "1234@naver.com", "1234", "이노", "01012345678", userAmount);
        //when
        int amount = user.chargeAmount(chargeAmount);
        //then
        assertThat(amount).isEqualTo(userAmount + chargeAmount);
    }


}