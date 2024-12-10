package concert.api.concert.request;

import lombok.Getter;

@Getter
public class ChargeRequest {
    private Long userId;
    private int amount;
}
