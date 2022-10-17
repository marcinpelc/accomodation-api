package mpelc.example.accommodation.domain.port.in;

import mpelc.example.accommodation.domain.model.Accommodation;
import mpelc.example.accommodation.domain.model.BookingWithIncome;

/** Provides functionality of calculating booking for given accommodation */
public interface CalculateBookingUseCase {
  BookingWithIncome calculateBookingWithIncome(Accommodation accommodation);
}
