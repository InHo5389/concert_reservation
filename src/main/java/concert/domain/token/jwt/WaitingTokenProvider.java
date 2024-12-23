package concert.domain.token.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import concert.domain.token.TokenStatus;
import concert.domain.token.entity.WaitingToken;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static concert.domain.token.jwt.WaitingTokenUtil.*;

@Component
public class WaitingTokenProvider {

    public String issueToken(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        String jwtToken = JWT.create()
                .withSubject("concert_token")
                .withExpiresAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .withClaim("userId", userId)
                .sign(Algorithm.HMAC512(SECRET_KEY));
        return TOKEN_PREFIX + jwtToken;
    }
}
