package mpelc.example.accommodation.domain.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mpelc.example.accommodation.domain.model.Accommodation;
import mpelc.example.accommodation.domain.model.Booking;
import mpelc.example.accommodation.domain.model.BookingWithIncome;
import mpelc.example.accommodation.domain.model.RankedGuests;
import mpelc.example.accommodation.domain.port.in.CalculateBookingUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService implements CalculateBookingUseCase {

  private final RulesService rulesService;

  @Override
  public BookingWithIncome calculateBookingWithIncome(Accommodation accommodation) {
    final List<BigDecimal> guestList = getGuestList();
    Booking booking = calculateBooking(accommodation, rankGuests(guestList));
    BigDecimal income =
        calculateIncome(booking, guestList, rulesService.getPremiumPricePredicate());
    return BookingWithIncome.builder()
        .income(income)
        .bookedEconomy(booking.getBookedEconomy())
        .bookedPremium(booking.getBookedPremium())
        .build();
  }

  BigDecimal calculateIncome(
      Booking booking, List<BigDecimal> guestsList, Predicate<BigDecimal> isCandidatePremium) {
    Function<Stream<BigDecimal>, Stream<BigDecimal>> getEconomy =
        (s) -> s.filter(isCandidatePremium.negate());

    Function<Stream<BigDecimal>, Stream<BigDecimal>> getQualifiedForPremium =
        (s) ->
            getEconomy
                .apply(s)
                .sorted(Comparator.reverseOrder())
                .limit(booking.getBookedPremiumByEconomy());

    BigDecimal lastQualifiedForPremium =
        getQualifiedForPremium
            .apply(guestsList.stream())
            .skip(
                booking.getBookedPremiumByEconomy() > 0
                    ? booking.getBookedPremiumByEconomy() - 1
                    : 0)
            .findFirst()
            // this will not work if value is greater than Integer.MAX_VALUE
            .orElse(BigDecimal.valueOf(Integer.MAX_VALUE));

    Stream<BigDecimal> qualifiedForEconomy =
        getEconomy
            .apply(guestsList.stream())
            .filter(v -> v.compareTo(lastQualifiedForPremium) < 0)
            .limit(booking.getBookedEconomy());
    BigDecimal earningsEconomy =
        qualifiedForEconomy.reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

    BigDecimal earningsStandardEconomy =
        getQualifiedForPremium
            .apply(guestsList.stream())
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO);

    BigDecimal earningsStandard = earningsEconomy.add(earningsStandardEconomy);

    Stream<BigDecimal> premium =
        guestsList.stream().filter(isCandidatePremium).limit(booking.getBookedPremium());
    BigDecimal earningsPremium = premium.reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

    return earningsStandard.add(earningsPremium);
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

  private List<BigDecimal> getGuestList() {
    return Stream.of("23", "45", "155", "374", "22", "99.99", "100", "101", "115", "209")
        .map(BigDecimal::new)
        .collect(Collectors.toList());
  }
}
