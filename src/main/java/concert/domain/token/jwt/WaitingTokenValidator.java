package concert.domain.token.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import concert.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingTokenValidator {

    public Long isTokenActive(String jwtToken) {
        if (jwtToken == null) {
            throw new BusinessException("토큰이 존재하지 않습니다.");
        }

        String token = removeBearer(jwtToken);
        DecodedJWT decodedJWT = verifyToken(token);
        Long userId = extractUserId(decodedJWT);

        return userId;
    }

    private String removeBearer(String token) {
        return token.substring(7);
    }

    private DecodedJWT verifyToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC512(WaitingTokenUtil.SECRET_KEY))
                    .build()
                    .verify(token);
        } catch (JWTVerificationException e) {
            throw new BusinessException("토큰이 유효하지 않습니다. 다시 예매하여 주세요.", e);
        }
    }

    private Long extractUserId(DecodedJWT decodedJWT) {
        return decodedJWT.getClaim("userId").asLong();
    }
}

