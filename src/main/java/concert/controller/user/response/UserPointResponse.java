package concert.controller.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPointResponse {

    private String username;
    private int amount;
}
