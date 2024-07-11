package concert.application;

import concert.domain.token.WaitingTokenService;
import concert.domain.token.dto.WaitingTokenIssueTokenDto;
import concert.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class WaitingTokenFacade {

    private final WaitingTokenService waitingTokenService;
    private final UserService userService;

    @Transactional
    public WaitingTokenIssueTokenDto issueToken(Long userId){
        userService.findUser(userId);
        return waitingTokenService.issueToken(userId);
    }
}
