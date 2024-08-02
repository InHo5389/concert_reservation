package concert.application;

import concert.domain.token.WaitingTokenService;
import concert.domain.token.dto.WaitingOrderDto;
import concert.domain.token.dto.WaitingTokenIssueTokenDto;
import concert.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingTokenFacade {

    private final WaitingTokenService waitingTokenService;
    private final UserService userService;

    @Transactional
    public void issueToken(Long userId){
        log.info("WaitingTokenFacade issueToken(): userId={}",userId);
        userService.getUser(userId);
        waitingTokenService.issueWaitingToken(userId);
    }

    @Transactional(readOnly = true)
    public WaitingOrderDto getWaitingOrder(Long userId){
        log.info("WaitingTokenFacade getWaitingOrder(): userId={}",userId);
        return waitingTokenService.getWaitingOrder(userId);
    }
}
