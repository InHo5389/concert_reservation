package concert.controller.token;

import concert.application.WaitingTokenFacade;
import concert.controller.token.response.IssueTokenResponse;
import concert.controller.token.response.WaitingTokenIssueTokenResponse;
import concert.domain.token.WaitingToken;
import concert.domain.token.WaitingTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final WaitingTokenFacade waitingTokenFacade;

    @PostMapping("/token/{userId}")
    public WaitingTokenIssueTokenResponse issueToken(@PathVariable Long userId){
        return WaitingTokenIssueTokenResponse.of(waitingTokenFacade.issueToken(userId));
    }
}
