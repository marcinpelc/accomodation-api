package mpelc.example.accommodation.inbound.mapper;

import mpelc.example.accommodation.domain.model.Accommodation;
import mpelc.example.accommodation.domain.model.BookingWithIncome;
import mpelc.example.accommodation.inbound.payload.AccommodationRequestPayload;
import mpelc.example.accommodation.inbound.payload.AccommodationResponsePayload;
import org.springframework.stereotype.Component;

@Component
public class AccommodationMapper {

  public Accommodation toAccommodation(AccommodationRequestPayload accommodationRequestPayload) {
    return Accommodation.builder()
        .economy(accommodationRequestPayload.getEconomy())
        .premium(accommodationRequestPayload.getPremium())
        .build();
  }

  public AccommodationResponsePayload toAccommodationResponsePayload(
      BookingWithIncome bookingWithIncome) {
    return AccommodationResponsePayload.builder()
        .income(bookingWithIncome.getIncome())
        .bookedEconomy(bookingWithIncome.getBookedEconomy())
        .bookedPremium(bookingWithIncome.getBookedPremium())
        .build();
  }
}
