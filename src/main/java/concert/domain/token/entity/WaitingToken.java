package concert.domain.token.entity;

import concert.domain.token.TokenStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitingToken {

    private Long userId;
    private double score;
    private TokenStatus tokenStatus;
    private LocalDateTime expiredAt;

    public static WaitingToken createWaitingToken(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        double score = Double.parseDouble(
                now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        );

        return WaitingToken.builder()
                .userId(userId)
                .tokenStatus(TokenStatus.WAIT)
                .score(score)
                .build();
    }

    public static WaitingToken createActiveToken(Long userId, int expirationMinutes) {
        LocalDateTime now = LocalDateTime.now();
        double score = Double.parseDouble(
                now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        );

        return WaitingToken.builder()
                .userId(userId)
                .tokenStatus(TokenStatus.ACTIVE)
                .expiredAt(now.plusMinutes(expirationMinutes))
                .score(score)
                .build();
    }

    public String formatForRedis() {
        return String.format("%d:%s",
                userId,
                expiredAt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        );
    }
}
