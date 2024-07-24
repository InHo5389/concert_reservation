package concert.infrastructure.user;

import concert.domain.user.AmountHistory;
import concert.domain.user.User;
import concert.domain.user.UserRepository;
import concert.infrastructure.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final AmountJpaHistory amountJpaHistory;
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
        return amountJpaHistory.save(amountHistory);
    }

    @Override
    public void deleteAllInBatch() {
        userJpaRepository.deleteAllInBatch();
    }
}
