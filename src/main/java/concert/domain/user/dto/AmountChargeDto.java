package concert.domain.user.dto;

import concert.domain.user.entity.AmountHistory;
import concert.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AmountChargeDto {
    User user;
    AmountHistory amountHistory;
}
