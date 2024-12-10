package concert.api.token.response;

import concert.domain.token.dto.WaitingOrderDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WaitingOrderResponse {

    private long waitingOrder;
    private boolean authority;

    public static WaitingOrderResponse of(WaitingOrderDto dto){
        return new WaitingOrderResponse(dto.getWaitingOrder(),dto.isAuthority());
    }
}
