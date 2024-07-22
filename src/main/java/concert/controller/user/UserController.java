package concert.controller.user;

import concert.application.UserFacade;
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
    public UserPointResponse getPoint( Long userId) {
        return UserPointResponse.of(userFacade.getAmount(userId));
    }


    @PostMapping("/amounts")
    public UserPointChargeResponse getChargePoint(Long userId, int amount) {
        return UserPointChargeResponse.of(userFacade.chargeAmount(userId, amount));
    }
}
