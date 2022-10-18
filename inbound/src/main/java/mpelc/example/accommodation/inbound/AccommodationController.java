package mpelc.example.accommodation.inbound;

import lombok.RequiredArgsConstructor;
import mpelc.example.accommodation.domain.model.Accommodation;
import mpelc.example.accommodation.domain.model.BookingWithIncome;
import mpelc.example.accommodation.domain.port.in.CalculateBookingUseCase;
import mpelc.example.accommodation.inbound.mapper.AccommodationMapper;
import mpelc.example.accommodation.inbound.payload.AccommodationRequestPayload;
import mpelc.example.accommodation.inbound.payload.AccommodationResponsePayload;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accommodation")
@RequiredArgsConstructor
public class AccommodationController {

  private final CalculateBookingUseCase calculateBookingUseCase;

  private final AccommodationMapper accommodationMapper;

  @PostMapping(value = "/bookingWithIncome", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<AccommodationResponsePayload> calculateBookingWithIncome(
      @RequestBody AccommodationRequestPayload accommodationRequestPayload) {
    Accommodation accommodation = accommodationMapper.toAccommodation(accommodationRequestPayload);
    BookingWithIncome bookingWithIncome =
        calculateBookingUseCase.calculateBookingWithIncome(accommodation);
    return ResponseEntity.ok(accommodationMapper.toAccommodationResponsePayload(bookingWithIncome));
  }
}
