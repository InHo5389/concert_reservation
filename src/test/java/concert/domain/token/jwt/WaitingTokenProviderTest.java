package concert.domain.token.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class WaitingTokenProviderTest {

    @Test
    @DisplayName("토큰을 발급한다.")
    void issueToken(){
        //given
        LocalDateTime now = LocalDateTime.now();
        //when
        WaitingTokenProvider tokenProvider = new WaitingTokenProvider();
        String jwtToken = tokenProvider.issueToken(1L, now, 5);
        //then
        assertThat(jwtToken).contains("Bearer");
        String[] split = jwtToken.split(" ");
        assertThat(split.length).isEqualTo(2);
    }

}