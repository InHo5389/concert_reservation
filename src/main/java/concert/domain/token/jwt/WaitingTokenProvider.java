package concert.domain.token.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static concert.domain.token.jwt.WatingTokenUtil.*;

@Component
public class WaitingTokenProvider {

    public String issueToken(Long userId, LocalDateTime now, int expirationMinutes) {

        String jwtToken = JWT.create()
                .withSubject("concert_token")
                .withExpiresAt(now.plusMinutes(expirationMinutes).atZone(ZoneId.systemDefault()).toInstant())
                .withClaim("userId", userId)
                .sign(Algorithm.HMAC512(SECRET_KEY));
        return TOKEN_PREFIX + jwtToken;
    }
}
