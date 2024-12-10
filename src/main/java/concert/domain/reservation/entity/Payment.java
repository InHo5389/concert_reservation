package concert.domain.reservation.entity;

import concert.domain.reservation.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reservationId;
    private int paymentAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static Payment createPayment(long reservationId, int amount){
        log.info("Payment create");

        LocalDateTime now = LocalDateTime.now();
        return Payment.builder()
                .reservationId(reservationId)
                .paymentAmount(amount)
                .status(PaymentStatus.COMPLETED)
                .createdAt(now)
                .modifiedAt(now)
                .build();
    }
}
