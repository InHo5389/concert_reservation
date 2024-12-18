package concert.infrastructure.user;

import concert.domain.user.entity.AmountHistory;
import concert.domain.user.entity.User;
import concert.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final AmountHistoryJpaRepository amountHistoryJpaRepository;
    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public AmountHistory save(AmountHistory amountHistory) {
        return amountHistoryJpaRepository.save(amountHistory);
    }


    @Override
    public void deleteAllInBatch() {
        userJpaRepository.deleteAllInBatch();
    }

    @Override
    public Optional<User> findByIdWithOptimisticLock(Long id) {
        return userJpaRepository.findByIdWithOptimisticLock(id);
    }

    @Override
    public Optional<User> findByIdWithPessimisticLock(Long id) {
        return userJpaRepository.findByIdWithPessimisticLock(id);
    }
}
