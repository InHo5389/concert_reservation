package concert.domain.token.jwt;

import concert.domain.token.TokenStatus;
import concert.domain.token.WaitingToken;
import concert.domain.token.WaitingTokenRepository;
import concert.domain.token.dto.WaitingOrderDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@SpringBootTest
class WaitingTokenValidatorTest {

    @Autowired
    private WaitingTokenRepository waitingTokenRepository;

    @Test
    @DisplayName("토큰을 검증할때 토큰이 유효하지 않으면 -1,false를 리턴한다.")
    void notVerify_fail(){
        //given
        LocalDateTime now = LocalDateTime.now();
        WaitingTokenProvider tokenProvider = new WaitingTokenProvider();
        String token = tokenProvider.issueToken(1L, now, 5);
        WaitingTokenValidator tokenValidator = new WaitingTokenValidator(waitingTokenRepository);

        waitingTokenRepository.save(new WaitingToken(1L,1L, TokenStatus.ACTIVE,now,now.plusMinutes(5)));
        //when
        WaitingOrderDto verify = tokenValidator.verify(token);
        //then
        Assertions.assertThat(verify).extracting("waitingOrder","authority")
                .contains(-1L,false);
    }

    @Test
    @DisplayName("토큰을 검증할때 그 토큰이 유효기간이 지나지 않고 ACTIVE 상태이면 0,ture를 리턴한다.")
    void verify_success() {
        //given
        LocalDateTime now = LocalDateTime.now();
        WaitingTokenProvider tokenProvider = new WaitingTokenProvider();
        String token = tokenProvider.issueToken(1L, now, 5);
        WaitingTokenValidator tokenValidator = new WaitingTokenValidator(waitingTokenRepository);

        waitingTokenRepository.save(new WaitingToken(1L, 1L, TokenStatus.ACTIVE, now, now.plusMinutes(5)));
        //when
        WaitingOrderDto verify = tokenValidator.verify(token.substring(7));
        //then
        Assertions.assertThat(verify).extracting("waitingOrder", "authority")
                .contains(0, true);
    }

    @Test
    @DisplayName("대기 순서를 구할때 나의 토큰id - active token마지막 id를 빼서 구한다.")
    void verify_fail2() {
        //given
        LocalDateTime now = LocalDateTime.now();
        WaitingTokenProvider tokenProvider = new WaitingTokenProvider();
        String token = tokenProvider.issueToken(4L, now, 5);
        WaitingTokenValidator tokenValidator = new WaitingTokenValidator(waitingTokenRepository);

        waitingTokenRepository.save(new WaitingToken(1L, 1L, TokenStatus.ACTIVE, now, now.plusMinutes(5)));
        waitingTokenRepository.save(new WaitingToken(2L, 2L, TokenStatus.ACTIVE, now.plusMinutes(1), now.plusMinutes(5)));
        waitingTokenRepository.save(new WaitingToken(3L, 3L, TokenStatus.ACTIVE, now.plusMinutes(2), now.plusMinutes(5)));
        waitingTokenRepository.save(new WaitingToken(4L, 4L, TokenStatus.WAIT, now.plusMinutes(3), now.plusMinutes(5)));
        //when
        WaitingOrderDto verify = tokenValidator.verify(token.substring(7));
        //then
        Assertions.assertThat(verify).extracting("waitingOrder", "authority")
                .contains(1L, false);
    }
}