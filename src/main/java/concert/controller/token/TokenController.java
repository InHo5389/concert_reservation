package concert.controller.token;

import concert.application.WaitingTokenFacade;
import concert.common.annotation.AuthUserId;
import concert.controller.token.response.IssueTokenResponse;
import concert.controller.token.response.WaitingOrderResponse;
import concert.controller.token.response.WaitingTokenIssueTokenResponse;
import concert.domain.token.WaitingToken;
import concert.domain.token.WaitingTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/concerts")
@RequiredArgsConstructor
public class TokenController {

    private final WaitingTokenFacade waitingTokenFacade;

    @PostMapping("/tokens/{userId}")
    public void issueToken(@PathVariable Long userId) {
        waitingTokenFacade.issueToken(userId);
    }

    @GetMapping("/tokens")
    public WaitingOrderResponse getWaitingOrder(@AuthUserId Long userId) {
        return WaitingOrderResponse.of(waitingTokenFacade.getWaitingOrder(userId));
    }
}
