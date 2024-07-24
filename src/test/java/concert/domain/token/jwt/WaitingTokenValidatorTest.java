package concert.domain.token.jwt;

import concert.common.exception.BusinessException;
import concert.domain.token.TokenStatus;
import concert.domain.token.WaitingToken;
import concert.domain.token.WaitingTokenRepository;
import concert.domain.token.WaitingTokenService;
import concert.domain.token.dto.WaitingOrderDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class WaitingTokenValidatorTest {

    @Autowired
    private WaitingTokenRepository waitingTokenRepository;

    @Autowired
    private WaitingTokenService waitingTokenService;

    @Test
    @DisplayName("토큰을 검증할때 토큰 만료시간이 지나면 예외가 발생한다.")
    void notVerify_fail(){
        //given
        LocalDateTime now = LocalDateTime.now();
        WaitingTokenProvider tokenProvider = new WaitingTokenProvider();
        String token = tokenProvider.issueToken(1L, now, -1);
        WaitingTokenValidator tokenValidator = new WaitingTokenValidator(waitingTokenRepository);

        waitingTokenRepository.save(new WaitingToken(1L,1L, TokenStatus.ACTIVE,now,now.plusMinutes(50)));
        //when
        //then
        assertThatThrownBy(()->tokenValidator.isTokenActive(token))
                .isInstanceOf(BusinessException.class)
                .hasMessage("토큰이 유효하지 않습니다. 다시 예매하여 주세요.");
    }

    @Test
    @DisplayName("토큰을 검증할때 그 토큰이 유효기간이 지나지 않고 ACTIVE 상태이면 0,ture를 리턴한다.")
    void verify_success() {
        //given
        LocalDateTime now = LocalDateTime.now();
        WaitingTokenProvider tokenProvider = new WaitingTokenProvider();
        long userId = 1L;
        String token = tokenProvider.issueToken(userId, now, 50);
        WaitingTokenValidator tokenValidator = new WaitingTokenValidator(waitingTokenRepository);

        waitingTokenRepository.save(new WaitingToken(userId, userId, TokenStatus.ACTIVE, now, now.plusMinutes(50)));
        //when
        Long getUserIdByTokenValidator = tokenValidator.isTokenActive(token);
        //then
        assertThat(getUserIdByTokenValidator).isEqualTo(userId);
    }

    @Test
    @DisplayName("대기 순서를 구할때 나의 토큰id - active token마지막 id를 빼서 구한다.")
    void verify_fail2() {
        //given
        LocalDateTime now = LocalDateTime.now();
        WaitingTokenProvider tokenProvider = new WaitingTokenProvider();
        long userId = 4L;
        String token = tokenProvider.issueToken(userId, now, 5);
        WaitingTokenValidator tokenValidator = new WaitingTokenValidator(waitingTokenRepository);

        waitingTokenRepository.save(new WaitingToken(1L, 1L, TokenStatus.ACTIVE, now, now.plusMinutes(5)));
        waitingTokenRepository.save(new WaitingToken(2L, 2L, TokenStatus.ACTIVE, now.plusMinutes(1), now.plusMinutes(5)));
        waitingTokenRepository.save(new WaitingToken(3L, 3L, TokenStatus.ACTIVE, now.plusMinutes(2), now.plusMinutes(5)));
        waitingTokenRepository.save(new WaitingToken(4L, userId, TokenStatus.WAIT, now.plusMinutes(3), now.plusMinutes(5)));
        //when
        WaitingOrderDto waitingOrder = waitingTokenService.getWaitingOrder(userId);
        //then
        assertThat(waitingOrder).extracting("waitingOrder", "authority")
                .contains(1L, false);
    }
}