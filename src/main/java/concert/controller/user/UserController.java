package concert.controller.user;

import concert.controller.user.response.UserPointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/points")
    public UserPointResponse getPoint(){
        return new UserPointResponse("정인호",10000);
    }

    @PostMapping("/points")
    public UserPointResponse getChargePoint(){
        return new UserPointResponse("정인호",20000);
    }


}
