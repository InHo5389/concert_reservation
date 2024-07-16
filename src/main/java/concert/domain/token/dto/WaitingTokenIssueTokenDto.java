package concert.domain.token.dto;

import concert.domain.token.TokenStatus;
import concert.domain.token.WaitingToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class WaitingTokenIssueTokenDto {

    private Long id;
    private Long userId;
    private TokenStatus tokenStatus;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private String jwtToken;

    public static WaitingTokenIssueTokenDto from(WaitingToken waitingToken,String jwtToken){
        return new WaitingTokenIssueTokenDto(waitingToken.getId(), waitingToken.getUserId(), waitingToken.getTokenStatus(),
                waitingToken.getCreatedAt(), waitingToken.getExpiredAt(), jwtToken);
    }
}
