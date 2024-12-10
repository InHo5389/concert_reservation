package concert.infrastructure.user;

import concert.domain.user.entity.AmountHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmountHistoryJpaRepository extends JpaRepository<AmountHistory,Long> {
    AmountHistory save(AmountHistory amountHistory);
}
