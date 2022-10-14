package mpelc.example.accomodation.domain.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mpelc.example.accomodation.domain.model.Accommodation;
import mpelc.example.accomodation.domain.model.Booking;
import mpelc.example.accomodation.domain.model.BookingWithIncome;
import mpelc.example.accomodation.domain.model.RankedGuests;
import mpelc.example.accomodation.domain.port.in.CalculateBookingUseCase;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService implements CalculateBookingUseCase {

  private final RulesService rulesService;

  @Override
  public BookingWithIncome calculateBookingWithIncome(Accommodation accommodation) {
    throw new IllegalStateException("Not implemented");
  }

  Booking calculateBooking(Accommodation accommodation, RankedGuests rankedGuests) {
    int premiumRoomsLeftAfterPremiumAccommodation =
        rankedGuests.getPremium() > accommodation.getPremium()
            ? 0
            : accommodation.getPremium() - rankedGuests.getPremium();
    int economyRoomsLeftAfterEconomyAccommodation =
        rankedGuests.getEconomy() > accommodation.getEconomy()
            ? 0
            : accommodation.getEconomy() - rankedGuests.getEconomy();
    int economyGuestsInPremiumRooms = 0;
    if (rankedGuests.getEconomy() > accommodation.getEconomy()
        && premiumRoomsLeftAfterPremiumAccommodation > 0) {
      int economyGuestsNotBooked = rankedGuests.getEconomy() - accommodation.getEconomy();
      economyGuestsInPremiumRooms =
              Math.min(economyGuestsNotBooked, premiumRoomsLeftAfterPremiumAccommodation);
    }
    return Booking.builder()
        .bookedEconomy(accommodation.getEconomy() - economyRoomsLeftAfterEconomyAccommodation)
        .bookedPremium(accommodation.getPremium() - premiumRoomsLeftAfterPremiumAccommodation)
        .bookedPremiumByEconomy(economyGuestsInPremiumRooms)
        .build();
  }

  RankedGuests rankGuests(@NonNull List<BigDecimal> guestsList) {
    final Map<Boolean, List<BigDecimal>> partitionedGuests =
        guestsList.stream()
            .collect(Collectors.partitioningBy(rulesService.getPremiumPricePredicate()));
    return RankedGuests.builder()
        .economy(partitionedGuests.get(Boolean.FALSE).size())
        .premium(partitionedGuests.get(Boolean.TRUE).size())
        .build();
  }
}
