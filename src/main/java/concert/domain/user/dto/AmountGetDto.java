package concert.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AmountGetDto {

    private String username;
    private int amount;
}
