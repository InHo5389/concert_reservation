package concert.api.user.response;

import concert.domain.user.dto.AmountChargeDto;
import concert.domain.user.AmountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPointChargeResponse {

    private String username;
    private int chargeAmount;
    private int remainAmount;
    private AmountStatus status;

    public static UserPointChargeResponse of(AmountChargeDto dto){
        return new UserPointChargeResponse(dto.getUser().getUsername(),dto.getAmountHistory().getUseAmount(),
                dto.getAmountHistory().getRemainAmount(),dto.getAmountHistory().getStatus());
    }
}
