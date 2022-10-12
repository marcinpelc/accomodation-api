package mpelc.example.accomodation.domain.port.in;

import mpelc.example.accomodation.domain.model.Accommodation;
import mpelc.example.accomodation.domain.model.Booking;

/** Provides functionality of calculating booking for given accommodation */
public interface CalculateBookingUseCase {
  Booking calculateBooking(Accommodation accommodation);
}
