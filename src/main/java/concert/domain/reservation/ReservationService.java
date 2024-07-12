package concert.domain.reservation;

import concert.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private static final int FIXED_EXPIRED_MINUTES = 5;

    @Transactional
    public Reservation reserveSeat(LocalDateTime concertDate, Long seatId){

        Reservation existingReservation  = reservationRepository.findByConcertDateAndSeatIdAndStatus(concertDate, seatId, ReservationStatus.RESERVED);
        if (existingReservation != null) {
            throw new BusinessException("좌석이 이미 예약되었습니다.");
        }

        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = Reservation.builder()
                .seatId(seatId)
                .concertDate(concertDate)
                .status(ReservationStatus.RESERVED)
                .createdAt(now)
                .modifiedAt(now)
                .expirationTime(now.plusMinutes(FIXED_EXPIRED_MINUTES))
                .build();

        try {
            return reservationRepository.save(reservation);
        }catch (DataIntegrityViolationException e){
            throw new BusinessException("동시에 같은 좌석을 예약하려는 시도가 있었습니다.");
        }
    }
}
