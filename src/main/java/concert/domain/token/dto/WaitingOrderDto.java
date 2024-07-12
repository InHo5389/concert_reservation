package concert.domain.token.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WaitingOrderDto {

    private long waitingOrder;
    private boolean authority;
}
