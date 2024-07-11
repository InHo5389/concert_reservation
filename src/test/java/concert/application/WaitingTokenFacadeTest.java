package concert.application;

import concert.common.exception.BusinessException;
import concert.domain.token.WaitingToken;
import concert.domain.token.WaitingTokenRepository;
import concert.domain.token.dto.WaitingTokenIssueTokenDto;
import concert.domain.token.jwt.WaitingTokenProvider;
import concert.domain.user.User;
import concert.domain.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
}