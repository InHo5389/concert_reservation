package concert.domain.token.dto;

import concert.domain.token.TokenStatus;
import concert.domain.token.entity.WaitingToken;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class WaitingTokenIssueTokenDto {

    private Long userId;
    private TokenStatus tokenStatus;
    private String jwtToken;

    public static WaitingTokenIssueTokenDto of(Long userId, TokenStatus tokenStatus, String jwtToken) {
        return new WaitingTokenIssueTokenDto(userId,tokenStatus,jwtToken);
    }
}
