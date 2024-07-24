package concert.application;


import concert.domain.concert.Concert;
import concert.domain.concert.ConcertRepository;
import concert.domain.concert.ConcertSchedule;
import concert.domain.concert.ConcertService;

import concert.common.exception.BusinessException;
import concert.domain.concert.*;
import concert.domain.token.TokenStatus;
import concert.domain.token.WaitingToken;
import concert.domain.token.WaitingTokenRepository;
import concert.domain.token.jwt.WaitingTokenProvider;
import concert.domain.token.jwt.WaitingTokenValidator;
import concert.domain.user.User;
import concert.domain.user.UserRepository;

import org.junit.jupiter.api.AfterEach;

import concert.infrastructure.token.WatingTokenJpaRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
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

        userRepository.save(new User(null,"12@naver.com","1234","인호","01012345678",500));

        Concert concert = concertRepository.save(new Concert(null, "임영웅", "임영웅"));

        concertRepository.save(new ConcertSchedule(null,concert.getId(), LocalDateTime.now().minusDays(1)));

        concertRepository.save(new ConcertSchedule(null,concert.getId(), LocalDateTime.now().plusDays(1)));
        concertRepository.save(new ConcertSchedule(null,concert.getId(), LocalDateTime.now().plusDays(1)));

        //when
        List<ConcertSchedule> concertSchedules = concertFacade.availableDates(concert.getId());
        LocalDateTime now = LocalDateTime.now();
        userRepository.save(new User(1L,"12@naver.com","1234","인호","01012345678",500));

        long concertId = 50L;
        concertRepository.save(new Concert(concertId,"임영웅","임영웅"));

        concertRepository.save(new ConcertSchedule(1L,concertId, LocalDateTime.now().minusDays(1)));

        concertRepository.save(new ConcertSchedule(2L,concertId, LocalDateTime.now().plusDays(1)));
        concertRepository.save(new ConcertSchedule(3L,concertId, LocalDateTime.now().plusDays(1)));

        String token = waitingTokenProvider.issueToken(1L, now, 5);
        waitingTokenRepository.save(new WaitingToken(1L,1L, TokenStatus.ACTIVE,now,now.plusMinutes(5)));
        //when
        List<ConcertSchedule> concertSchedules = concertFacade.availableDates(concertId);
        //then
        assertThat(concertSchedules).hasSize(2);
    }

    @Test
    @DisplayName("현재 일시에 콘서트 스케줄을 조회할때 스케줄이 지나지 않은 콘서트 스케줄만 조회된다.")
    void getConcertSchedule_FAIL(){
        //given
        userRepository.save(new User(null,"12@naver.com","1234","인호","01012345678",500));

        Concert concert = concertRepository.save(new Concert(null, "임영웅 콘서트", "임영웅"));
        concertRepository.save(new ConcertSchedule(null,concert.getId(), LocalDateTime.now().minusDays(1)));

        concertRepository.save(new ConcertSchedule(null,concert.getId(), LocalDateTime.now().plusDays(1)));
        concertRepository.save(new ConcertSchedule(null,concert.getId(), LocalDateTime.now().plusDays(1)));

        //when
        List<ConcertSchedule> concertSchedules = concertFacade.availableDates(concert.getId());
        LocalDateTime now = LocalDateTime.now();
        userRepository.save(new User(1L,"12@naver.com","1234","인호","01012345678",500));

        concertRepository.save(new Concert(1L,"임영웅 콘서트" , "임영웅"));
        concertRepository.save(new ConcertSchedule(1L,1L, LocalDateTime.now().minusDays(1)));

        concertRepository.save(new ConcertSchedule(2L,1L, LocalDateTime.now().plusDays(1)));
        concertRepository.save(new ConcertSchedule(3L,1L, LocalDateTime.now().plusDays(1)));

        String token = waitingTokenProvider.issueToken(1L, now, 5);
        waitingTokenRepository.save(new WaitingToken(1L,1L, TokenStatus.WAIT,now,now.plusMinutes(5)));
        //when
        List<ConcertSchedule> concertSchedules = concertFacade.availableDates(1L);
        //then
        assertThat(concertSchedules.size()).isEqualTo(2);
    }
}