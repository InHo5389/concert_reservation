package concert.controller.user;

import concert.application.UserFacade;
import concert.controller.user.response.UserPointChargeResponse;
import concert.controller.user.response.UserPointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @GetMapping(value = "/amounts", headers = "Authorization")
    public UserPointResponse getPoint(@RequestHeader(name = "Authorization") String jwtToken, Long userId) {
        return UserPointResponse.of(userFacade.getAmount(userId, jwtToken));
    }


    @PostMapping(value = "/amounts", headers = "Authorization")
    public UserPointChargeResponse getChargePoint(@RequestHeader(name = "Authorization") String jwtToken, Long userId, int amount) {
        return UserPointChargeResponse.of(userFacade.chargeAmount(userId, amount, jwtToken));
    }
}
