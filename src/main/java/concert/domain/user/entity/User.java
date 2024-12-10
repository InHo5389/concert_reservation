package concert.domain.user.entity;

import concert.common.exception.BusinessException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Entity
@Builder
@Table(name = "users")
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
        log.info("chargeAmount(): username={},amount={}",this.username,amount);
        return this.amount += amount;
    }

    public void validateAndDecreaseAmount(long amount){
        if(!availablePay(amount)){
            log.warn("Not Enough Amount id={}, username={}",this.id,this.username);
            throw new BusinessException("잔액이 부족합니다.");
        }
        this.amount -= amount;
    }
    public boolean availablePay(long amount){
        return this.amount >= amount;
    }
}
