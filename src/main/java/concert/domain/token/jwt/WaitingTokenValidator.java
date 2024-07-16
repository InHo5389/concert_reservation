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

//    public WaitingOrderDto verify(String jwt) {
//        DecodedJWT decodedJWT = null;
//        try {
//            decodedJWT = JWT.require(Algorithm.HMAC512("concert"))
//                    .build()
//                    .verify(jwt);
//        } catch (JWTDecodeException | TokenExpiredException | NullPointerException e) {
//            return new WaitingOrderDto(-1, false);
//        }
//        Long userId = getUserIdFromToken(decodedJWT);
//        WaitingToken waitingToken = waitingTokenRepository.findByUserId(userId).get();
//
//        if (waitingToken.getTokenStatus().equals(TokenStatus.ACTIVE)) {
//            return new WaitingOrderDto(0, true);
//        }
//
//        long lastActiveTokenNum = waitingTokenRepository.findLastActiveTokenBy(userId);
//
//        log.info(String.valueOf(lastActiveTokenNum));
//
//        long waitingOrder = waitingToken.getId() - lastActiveTokenNum;
//        return new WaitingOrderDto(waitingOrder, false);
//    }

    public Long validateTokenAndGetUserId(String jwt) {
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = JWT.require(Algorithm.HMAC512("concert"))
                    .build()
                    .verify(jwt);
            return decodedJWT.getClaim("userId").asLong();
        } catch (JWTDecodeException | TokenExpiredException | NullPointerException e) {
            throw new BusinessException("토큰이 유효하지 않습니다.", e);
        }
    }

    public void isTokenActive(String jwtToken) {
        jwtToken = jwtToken.substring(7);
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = JWT.require(Algorithm.HMAC512("concert"))
                    .build()
                    .verify(jwtToken);
        } catch (JWTDecodeException | TokenExpiredException | NullPointerException e) {
            throw new BusinessException("토큰이 유효하지 않습니다. 다시 예매하여 주세요.");
        }

        Long userId = getUserIdFromToken(decodedJWT);

        WaitingToken waitingToken = waitingTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException("토큰이 존재하지 않습니다."));

        if (waitingToken.getTokenStatus().equals(TokenStatus.EXPIRED)) {
            throw new BusinessException("토큰이 만료되었습니다.");
        } else if (waitingToken.getTokenStatus().equals(TokenStatus.WAIT)) {
            throw new BusinessException("대기열 대기중입니다. 잠시만 기다려주세요.");
        }
    }

    private Long getUserIdFromToken(DecodedJWT decodedJWT) {
        return decodedJWT.getClaim("userId").asLong();
    }
}

