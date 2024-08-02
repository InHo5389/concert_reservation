package concert.domain.user;

import concert.common.exception.BusinessException;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User getUser(Long userId) {
        log.info("getUser()");
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("회원을 찾을수 없습니다."));
    }

    public User getUserWithOptimisticLock(Long userId) {
        return userRepository.findByIdWithOptimisticLock(userId)
                .orElseThrow(() -> new BusinessException("회원을 찾을수 없습니다."));
    }

    public User getUserWithPessimisticLock(Long userId) {
        return userRepository.findByIdWithPessimisticLock(userId)
                .orElseThrow(() -> new BusinessException("회원을 찾을수 없습니다."));
    }

    @Transactional
    public void processPayment(Long userId, int amount) {
        log.info("UserService processPayment(): userId={}, amount={}", userId, amount);
        User user = getUser(userId);
        user.validateAndDecreaseAmount(amount);
        userRepository.save(user);
        saveAmountHistory(user, amount);
    }

    public AmountGetDto getAmount(Long userId) {
        User user = getUser(userId);
        return new AmountGetDto(user.getUsername(), user.getAmount());
    }

    @Transactional
    public AmountChargeDto chargeAmount(Long userId, int amount) {
        log.info("UserService chargeAmount(): userId={}, amount={}", userId, amount);

        User user = getUserWithPessimisticLock(userId);

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
        log.info("UserService saveAmountHistory(): userId={}, amount={}", user.getId(), amount);
        AmountHistory amountHistory = AmountHistory.builder()
                .userId(user.getId())
                .useAmount(amount)
                .remainAmount(user.getAmount())
                .status(AmountStatus.USE)
                .build();
        userRepository.save(amountHistory);
    }
}
