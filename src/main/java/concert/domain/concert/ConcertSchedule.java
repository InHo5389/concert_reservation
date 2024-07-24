package concert.domain.concert;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long concertId;
    private LocalDateTime concertDateTime;

    public boolean isAvailableConcert(LocalDateTime now){
        return concertDateTime.isAfter(now);
    }


    public static ConcertSchedule create(Long concertId,LocalDateTime concertDateTime){
        return ConcertSchedule.builder()
                .concertId(concertId)
                .concertDateTime(concertDateTime)
                .build();
    }
}
