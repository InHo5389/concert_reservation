package concert.application;

import concert.common.exception.BusinessException;
import concert.domain.concert.*;
import concert.domain.token.TokenStatus;
import concert.domain.token.WaitingToken;
import concert.domain.token.WaitingTokenRepository;
import concert.domain.token.jwt.WaitingTokenProvider;
import concert.domain.token.jwt.WaitingTokenValidator;
import concert.domain.user.User;
import concert.domain.user.UserRepository;
import concert.infrastructure.token.WatingTokenJpaRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConcertFacadeTest {

    @Autowired
    private ConcertFacade concertFacade;
    @Autowired
    private ConcertService concertService;

    @Autowired
    private WaitingTokenValidator waitingTokenValidator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WaitingTokenProvider waitingTokenProvider;

    @Autowired
    private WaitingTokenRepository waitingTokenRepository;

    @Autowired
    private ConcertRepository concertRepository;


    @Test
    @DisplayName("토큰이 Active상태일때 콘서트 스케줄을 조회할 수 있다..")
    void getConcertSchedule(){
        //given
        LocalDateTime now = LocalDateTime.now();
        userRepository.save(new User(1L,"12@naver.com","1234","인호","01012345678",500));

        concertRepository.save(new Concert(1L,"임영웅 콘서트" , "임영웅"));

        concertRepository.save(new ConcertSchedule(1L,1L, LocalDateTime.now().minusDays(1)));

        concertRepository.save(new ConcertSchedule(2L,1L, LocalDateTime.now().plusDays(1)));
        concertRepository.save(new ConcertSchedule(3L,1L, LocalDateTime.now().plusDays(1)));

        String token = waitingTokenProvider.issueToken(1L, now, 5);
        waitingTokenRepository.save(new WaitingToken(1L,1L, TokenStatus.ACTIVE,now,now.plusMinutes(5)));
        //when
        List<ConcertSchedule> concertSchedules = concertFacade.availableDates(1L, token);
        //then
        assertThat(concertSchedules).hasSize(2);
    }

    @Test
    @DisplayName("토큰이 WAIT 상태일때 콘서트 스케줄을 조회하면 예외가 발생한다.")
    void getConcertSchedule_FAIL(){
        //given
        LocalDateTime now = LocalDateTime.now();
        userRepository.save(new User(1L,"12@naver.com","1234","인호","01012345678",500));

        concertRepository.save(new Concert(1L,"임영웅 콘서트" , "임영웅"));

        concertRepository.save(new ConcertSchedule(1L,1L, LocalDateTime.now().minusDays(1)));

        concertRepository.save(new ConcertSchedule(2L,1L, LocalDateTime.now().plusDays(1)));
        concertRepository.save(new ConcertSchedule(3L,1L, LocalDateTime.now().plusDays(1)));

        String token = waitingTokenProvider.issueToken(1L, now, 5);
        waitingTokenRepository.save(new WaitingToken(1L,1L, TokenStatus.WAIT,now,now.plusMinutes(5)));
        //when
        //then
        assertThatThrownBy(()->concertFacade.availableDates(1L, token))
                .isInstanceOf(BusinessException.class)
                .hasMessage("대기열 대기중입니다. 잠시만 기다려주세요.");
    }
}