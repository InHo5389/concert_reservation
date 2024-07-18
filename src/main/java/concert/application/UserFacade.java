package concert.application;

import concert.domain.token.WaitingTokenService;
import concert.domain.token.jwt.WaitingTokenValidator;
import concert.domain.user.AmountChargeDto;
import concert.domain.user.AmountGetDto;
import concert.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public AmountChargeDto chargeAmount(Long userId, int amount) {
        return userService.chargeAmount(userId, amount);
    }

    public AmountGetDto getAmount(Long userId) {
        return userService.getAmount(userId);
    }
}
