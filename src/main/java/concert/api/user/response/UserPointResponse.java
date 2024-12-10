package concert.api.user.response;

import concert.domain.user.dto.AmountGetDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPointResponse {

    private String username;
    private int amount;

    public static UserPointResponse of(AmountGetDto dto){
        return new UserPointResponse(dto.getUsername(), dto.getAmount());
    }
}
