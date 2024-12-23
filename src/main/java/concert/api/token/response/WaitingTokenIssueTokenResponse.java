package concert.api.token.response;

import concert.domain.token.TokenStatus;
import concert.domain.token.dto.WaitingTokenIssueTokenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitingTokenIssueTokenResponse {

    private Long userId;
    private TokenStatus tokenStatus;
    private String jwtToken;

    public static WaitingTokenIssueTokenResponse of(WaitingTokenIssueTokenDto dto){
        return WaitingTokenIssueTokenResponse.builder()
                .userId(dto.getUserId())
                .tokenStatus(dto.getTokenStatus())
                .jwtToken(dto.getJwtToken())
                .build();
    }
}
