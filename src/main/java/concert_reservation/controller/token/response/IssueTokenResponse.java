package concert_reservation.controller.token.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class IssueTokenResponse {

    private String userName;
    private String tokenStatus;
    private LocalDateTime expiredAt;
}
