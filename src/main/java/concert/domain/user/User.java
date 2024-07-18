package concert.domain.user;

import concert.common.exception.BusinessException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String username;
    private String phone;
    private int amount;

    public int chargeAmount(long amount) {
        return this.amount += amount;
    }

    public void validateAndDecreaseAmount(long amount){
        if(!availablePay(amount)){
            throw new BusinessException("잔액이 부족합니다.");
        }
        this.amount -= amount;
    }
    public boolean availablePay(long amount){
        return this.amount >= amount;
    }
}
