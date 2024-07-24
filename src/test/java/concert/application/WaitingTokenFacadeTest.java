package concert.application;

import concert.common.exception.BusinessException;
import concert.domain.token.TokenStatus;
import concert.domain.token.WaitingToken;
import concert.domain.token.WaitingTokenRepository;
import concert.domain.token.dto.WaitingOrderDto;
import concert.domain.token.dto.WaitingTokenIssueTokenDto;
import concert.domain.token.jwt.WaitingTokenProvider;
import concert.domain.user.User;
import concert.domain.user.UserRepository;
import org.assertj.core.api.Assertions;
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
class WaitingTokenFacadeTest {

    @Autowired
    private WaitingTokenFacade waitingTokenFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WaitingTokenRepository waitingTokenRepository;

    @Test
    @DisplayName("토큰을 발급하면 대기열 토큰 db에 저장된다..")
    void issueToken(){
        //given
        User user = userRepository.save(new User(1L, "12@naver.com", "1234", "인호", "01012345678", 5000));
        //when
        WaitingTokenIssueTokenDto issueTokenDto = waitingTokenFacade.issueToken(user.getId());
        WaitingToken waitingToken = waitingTokenRepository.findByUserId(user.getId()).get();
        //then
        assertThat(issueTokenDto.getId()).isEqualTo(waitingToken.getId());
        assertThat(issueTokenDto.getUserId()).isEqualTo(waitingToken.getUserId());
        assertThat(issueTokenDto.getJwtToken()).contains("Bearer ");
    }

    @Test
    @DisplayName("토큰을 발급할때 발급할 유저id가 db에 저장되어 있지 않으면 오류가 발생한다.")
    void issueTokenNotUser(){
        //given
        //when
        //then
        assertThatThrownBy(()->waitingTokenFacade.issueToken(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("회원을 찾을수 없습니다.");
    }

    @Test
    @DisplayName("대기 순서를 구할때 나의 토큰id - active token마지막 id를 빼서 구한다.")
    void getWaitingOrder(){
        //given
        LocalDateTime now = LocalDateTime.now();
        WaitingTokenProvider tokenProvider = new WaitingTokenProvider();
        long userId = 4L;

        waitingTokenRepository.save(new WaitingToken(1L, 1L, TokenStatus.ACTIVE, now, now.plusMinutes(5)));
        waitingTokenRepository.save(new WaitingToken(2L, 2L, TokenStatus.ACTIVE, now.plusMinutes(1), now.plusMinutes(5)));
        waitingTokenRepository.save(new WaitingToken(3L, 3L, TokenStatus.ACTIVE, now.plusMinutes(2), now.plusMinutes(5)));
        waitingTokenRepository.save(new WaitingToken(userId, userId, TokenStatus.WAIT, now.plusMinutes(3), now.plusMinutes(5)));
        //when
        WaitingOrderDto waitingOrder = waitingTokenFacade.getWaitingOrder(userId);
        //then
        Assertions.assertThat(waitingOrder).extracting("waitingOrder", "authority")
                .contains(1L, false);
    }

    @Test
    @DisplayName("토큰을 검증할때 그 토큰이 유효기간이 지나지 않고 ACTIVE 상태이면 0,ture를 리턴한다.")
    void getWaitingOrderStatusActive() {
        //given
        LocalDateTime now = LocalDateTime.now();
        WaitingTokenProvider tokenProvider = new WaitingTokenProvider();

        long userId = 1L;
        waitingTokenRepository.save(new WaitingToken(1L, userId, TokenStatus.ACTIVE, now, now.plusMinutes(5)));
        //when
        WaitingOrderDto waitingOrder = waitingTokenFacade.getWaitingOrder(userId);
        //then
        Assertions.assertThat(waitingOrder).extracting("waitingOrder", "authority")
                .contains(0L, true);
    }
}