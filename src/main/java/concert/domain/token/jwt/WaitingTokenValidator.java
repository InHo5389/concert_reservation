package concert.domain.token.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import concert.common.exception.BusinessException;
import concert.domain.token.TokenStatus;
import concert.domain.token.WaitingToken;
import concert.domain.token.WaitingTokenRepository;
import concert.domain.token.dto.WaitingOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingTokenValidator {

    private final WaitingTokenRepository waitingTokenRepository;

    public WaitingOrderDto verify(String jwt) {
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = JWT.require(Algorithm.HMAC512("concert"))
                    .build()
                    .verify(jwt);
        } catch (JWTDecodeException | TokenExpiredException | NullPointerException e) {
            return new WaitingOrderDto(-1,false);
        }
        Long userId = getUserIdFromToken(decodedJWT);
        WaitingToken waitingToken = waitingTokenRepository.findByUserId(userId).get();

        if (waitingToken.getTokenStatus().equals(TokenStatus.ACTIVE)){
            return new WaitingOrderDto(0,true);
        }

        long lastActiveTokenNum = waitingTokenRepository.findLastActiveTokenBy(userId);

        log.info(String.valueOf(lastActiveTokenNum));

        long waitingOrder = waitingToken.getId() - lastActiveTokenNum;
        return new WaitingOrderDto(waitingOrder,false);
    }

    private Long getUserIdFromToken(DecodedJWT decodedJWT) {
        return decodedJWT.getClaim("userId").asLong();
    }
}

