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


    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("회원을 찾을수 없습니다."));
    }

    @Transactional
    public void processPayment(Long userId, int amount) {
        User user = getUser(userId);
        user.validateAndDecreaseAmount(amount);
        userRepository.save(user);
        saveAmountHistory(user, amount);
    }

    public AmountGetDto getAmount(Long userId) {
        User user = getUser(userId);
        return new AmountGetDto(user.getUsername(), user.getAmount());
    }

    public AmountChargeDto chargeAmount(Long userId, int amount) {
        User user = getUser(userId);
        int savedAmount = user.chargeAmount(amount);

        AmountHistory amountHistory = userRepository.save(AmountHistory.builder()
                .userId(userId)
                .remainAmount(savedAmount)
                .useAmount(amount)
                .status(AmountStatus.CHARGE)
                .build());
        return new AmountChargeDto(user, amountHistory);
    }

    private void saveAmountHistory(User user, int amount) {
        AmountHistory amountHistory = AmountHistory.builder()
                .userId(user.getId())
                .useAmount(amount)
                .remainAmount(user.getAmount())
                .status(AmountStatus.USE)
                .build();
        userRepository.save(amountHistory);
    }
}
