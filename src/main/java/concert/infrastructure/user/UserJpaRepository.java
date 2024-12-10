package concert.infrastructure.user;

import concert.domain.user.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select u from User u where id = :id")
    Optional<User> findByIdWithOptimisticLock(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where id = :id")
    Optional<User> findByIdWithPessimisticLock(Long id);
}
