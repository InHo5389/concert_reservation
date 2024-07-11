package concert.domain.token.dto;

import concert.domain.token.TokenStatus;
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
}
