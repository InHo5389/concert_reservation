package concert.domain.token;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitingToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private TokenStatus tokenStatus;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    public static WaitingToken issue(Long userId, int activeTokenCount,int fixActiveCount ,int expirationMinutes) {
        LocalDateTime now = LocalDateTime.now();
        return WaitingToken.builder()
                .userId(userId)
                .tokenStatus(validTokenStatus(activeTokenCount,fixActiveCount))
                .createdAt(now)
                .expiredAt(now.plusMinutes(expirationMinutes))
                .build();
    }

    private static TokenStatus validTokenStatus(int activeTokenCount,int fixActiveCount) {
        return activeTokenCount < fixActiveCount ? TokenStatus.ACTIVE : TokenStatus.WAIT;
    }

    public void activate() {
        this.tokenStatus = TokenStatus.ACTIVE;
    }

    public void expire() {
        this.tokenStatus = TokenStatus.EXPIRED;
    }
}
