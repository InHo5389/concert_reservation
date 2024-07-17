package concert.domain.token;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class WaitingTokenTest {

    @Test
    @DisplayName("토큰을 발급한다.")
    void issue(){
        //given
        LocalDateTime now = LocalDateTime.now();
        long userId = 1L;
        int expirationMinutes = 5;
        //when
        WaitingToken issuedToken = WaitingToken.issue(now, userId, 2, 3, expirationMinutes);
        //then
        assertThat(issuedToken).extracting("userId","tokenStatus","createdAt","expiredAt")
                .contains(userId,TokenStatus.ACTIVE,now,now.plusMinutes(expirationMinutes));
    }

    @Test
    @DisplayName("토큰을 발급할때 정해진 active 개수보다 db active 개수가 적으면 발급 토큰 상태는 ACTIVE이다.")
    void issue_active(){
        //given
        LocalDateTime now = LocalDateTime.now();
        //when
        WaitingToken issuedToken = WaitingToken.issue(now, 1L, 2, 3, 5);
        //then
        assertThat(issuedToken.getTokenStatus()).isEqualByComparingTo(TokenStatus.ACTIVE);
    }

    @Test
    @DisplayName("토큰을 발급할때 정해진 active 개수보다 db active 개수가 많으면 발급 토큰 상태는 WAIT이다.")
    void issue_wait(){
        //given
        LocalDateTime now = LocalDateTime.now();
        //when
        WaitingToken issuedToken = WaitingToken.issue(now, 1L, 3, 2, 5);
        //then
        assertThat(issuedToken.getTokenStatus()).isEqualByComparingTo(TokenStatus.WAIT);
    }

    @Test
    @DisplayName("토큰을 발급할때 토큰 발급 시간은 현재시간 더하기 5분이다.")
    void issuedTokenExpiredAt(){
        //given
        LocalDateTime now = LocalDateTime.now();
        //when
        WaitingToken issuedToken = WaitingToken.issue(now, 1L, 3, 2, 5);
        //then
        assertThat(issuedToken.getExpiredAt()).isEqualTo(now.plusMinutes(5));
    }

}