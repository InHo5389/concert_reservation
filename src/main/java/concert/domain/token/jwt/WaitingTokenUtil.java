package concert.domain.token.jwt;

public interface WaitingTokenUtil {
    String SECRET_KEY = "concert";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER = "WaitingToken";
}
