package concert.domain.token.entity;

import concert.domain.token.TokenStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
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

    public static WaitingToken issue(Long userId, int activeTokenCount,
                                     int fixActiveCount, int expirationMinutes) {
        log.info("WaitingToken issue: userId={}", userId);
        LocalDateTime now = LocalDateTime.now();
        TokenStatus tokenStatus = determineTokenStatus(activeTokenCount, fixActiveCount);

        return WaitingToken.builder()
                .userId(userId)
                .tokenStatus(tokenStatus)
                .createdAt(now)
                .expiredAt(now.plusMinutes(expirationMinutes))
                .build();
    }

    private static TokenStatus determineTokenStatus(int activeTokenCount, int fixActiveCount) {
        return activeTokenCount < fixActiveCount ? TokenStatus.ACTIVE : TokenStatus.WAIT;
    }

    public void activate(int expirationMinutes) {
        LocalDateTime now = LocalDateTime.now();
        this.tokenStatus = TokenStatus.ACTIVE;
        this.expiredAt = now.plusMinutes(expirationMinutes);
    }

    public void expire() {
        this.tokenStatus = TokenStatus.EXPIRED;
        this.expiredAt = LocalDateTime.now();
    }
}
