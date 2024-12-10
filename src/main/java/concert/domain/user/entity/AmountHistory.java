package concert.domain.user.entity;

import concert.domain.user.AmountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmountHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private int useAmount;
    private int remainAmount;

    @Enumerated(EnumType.STRING)
    private AmountStatus status;

}
