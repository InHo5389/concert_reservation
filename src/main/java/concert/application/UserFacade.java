package concert.application;

import concert.domain.user.dto.AmountChargeDto;
import concert.domain.user.dto.AmountGetDto;
import concert.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public AmountChargeDto chargeAmount(Long userId, int amount) {
        log.info("UserFacade chargeAmount(): userId={}",userId);
        return userService.chargeAmount(userId, amount);
    }

    public AmountGetDto getAmount(Long userId) {
        log.info("UserFacade getAmount(): userId={}",userId);
        return userService.getAmount(userId);
    }
}
