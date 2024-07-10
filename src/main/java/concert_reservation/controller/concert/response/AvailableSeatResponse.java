package concert_reservation.controller.concert.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AvailableSeatResponse {

    private int seatNumber;
    private int seatPrice;
    private String seatStatus;

}
