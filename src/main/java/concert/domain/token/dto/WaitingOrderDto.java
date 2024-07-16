package concert.domain.token.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WaitingOrderDto {

    private long waitingOrder;
    private boolean authority;

    public static WaitingOrderDto active() {
        return new WaitingOrderDto(0, true);
    }

    public static WaitingOrderDto waiting(long order) {
        return new WaitingOrderDto(order, false);
    }

    public static WaitingOrderDto invalid() {
        return new WaitingOrderDto(-1, false);
    }
}
