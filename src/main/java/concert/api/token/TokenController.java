package concert.api.token;

import concert.application.WaitingTokenFacade;
import concert.common.annotation.AuthUserId;
import concert.api.token.response.WaitingOrderResponse;
import concert.api.token.response.WaitingTokenIssueTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/concerts")
@RequiredArgsConstructor
public class TokenController {

    private final WaitingTokenFacade waitingTokenFacade;

    @PostMapping("/tokens/{userId}")
    public WaitingTokenIssueTokenResponse issueToken(@PathVariable Long userId) {
        return WaitingTokenIssueTokenResponse.of(waitingTokenFacade.issueToken(userId));
    }

    @GetMapping("/tokens")
    public WaitingOrderResponse getWaitingOrder(@AuthUserId Long userId) {
        return WaitingOrderResponse.of(waitingTokenFacade.getWaitingOrder(userId));
    }
}
