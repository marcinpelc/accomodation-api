package mpelc.example.accomodation.domain.service;

import mpelc.example.accomodation.domain.model.Accommodation;
import mpelc.example.accomodation.domain.model.Booking;
import mpelc.example.accomodation.domain.port.in.CalculateBookingUseCase;

public class BookingService implements CalculateBookingUseCase {
  @Override
  public Booking calculateBooking(Accommodation accommodation) {
    throw new IllegalStateException("Not implemented");
  }
}
