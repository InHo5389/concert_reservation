package concert.domain.token.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import concert.domain.token.entity.WaitingToken;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;

import static concert.domain.token.jwt.WaitingTokenUtil.*;

@Component
public class WaitingTokenProvider {

    public String issueToken(WaitingToken waitingToken) {

        String jwtToken = JWT.create()
                .withSubject("concert_token")
                .withExpiresAt(Date.from(waitingToken.getExpiredAt().atZone(ZoneId.systemDefault()).toInstant()))
                .withClaim("userId", waitingToken.getUserId())
                .sign(Algorithm.HMAC512(SECRET_KEY));
        return TOKEN_PREFIX + jwtToken;
    }
}
