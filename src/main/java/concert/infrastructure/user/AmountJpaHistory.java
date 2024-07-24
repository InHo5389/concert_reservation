package concert.infrastructure.user;

import concert.domain.user.AmountHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmountJpaHistory extends JpaRepository<AmountHistory,Long> {
    AmountHistory save(AmountHistory amountHistory);
}
