package concert.domain.user;

import concert.domain.user.entity.AmountHistory;
import concert.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    User save(User user);

    AmountHistory save(AmountHistory amountHistory);
    void deleteAllInBatch();
    Optional<User> findByIdWithOptimisticLock(Long id);
    Optional<User> findByIdWithPessimisticLock(Long id);
}
