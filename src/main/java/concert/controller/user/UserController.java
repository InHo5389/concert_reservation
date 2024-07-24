package concert.controller.user;

import concert.application.UserFacade;
import concert.common.annotation.AuthUserId;
import concert.controller.concert.request.ChargeRequest;
import concert.controller.user.response.UserPointChargeResponse;
import concert.controller.user.response.UserPointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/concerts")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @GetMapping("/amounts")
    public UserPointResponse getPoint(@AuthUserId Long userId) {
        return UserPointResponse.of(userFacade.getAmount(userId));
    }


    @PostMapping("/amounts")
    public UserPointChargeResponse getChargePoint(@RequestBody ChargeRequest request) {
        return UserPointChargeResponse.of(userFacade.chargeAmount(request.getUserId(), request.getAmount()));
    }
}
