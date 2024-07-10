package concert.controller.token;

import concert.controller.token.response.IssueTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class TokenController {

    @PostMapping("/request-token")
    public IssueTokenResponse issueToken(){
        return new IssueTokenResponse("정인호","ACTIVE", LocalDateTime.now().plusHours(1));
    }
}
