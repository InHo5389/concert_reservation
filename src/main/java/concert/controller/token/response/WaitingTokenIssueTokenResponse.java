package concert.controller.token.response;

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

    private Long id;
    private Long userId;
    private TokenStatus tokenStatus;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private String jwtToken;

    public static WaitingTokenIssueTokenResponse of(WaitingTokenIssueTokenDto dto){
        return WaitingTokenIssueTokenResponse.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .tokenStatus(dto.getTokenStatus())
                .createdAt(dto.getCreatedAt())
                .expiredAt(dto.getExpiredAt())
                .jwtToken(dto.getJwtToken())
                .build();
    }
}
