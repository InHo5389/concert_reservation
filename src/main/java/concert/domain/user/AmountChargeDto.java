package concert.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AmountChargeDto {
    User user;
    AmountHistory amountHistory;
}
