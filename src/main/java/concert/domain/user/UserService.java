package concert.domain.user;

import concert.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("회원을 찾을수 없습니다."));
    }

    public AmountGetDto getAmount(Long userId){
        User user = findUser(userId);
        return new AmountGetDto(user.getUsername(),user.getAmount());
    }

    public AmountChargeDto chargeAmount(Long userId, int amount){
        User user = findUser(userId);
        int savedAmount = user.chargeAmount(amount);

        AmountHistory amountHistory = userRepository.save(AmountHistory.builder()
                .userId(userId)
                .remainAmount(savedAmount)
                .useAmount(amount)
                .status(AmountStatus.CHARGE)
                .build());
        return new AmountChargeDto(user,amountHistory);
    }
}
