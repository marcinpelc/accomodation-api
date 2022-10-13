package mpelc.example.accomodation.domain.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mpelc.example.accomodation.domain.model.Accommodation;
import mpelc.example.accomodation.domain.model.Booking;
import mpelc.example.accomodation.domain.model.RankedGuests;
import mpelc.example.accomodation.domain.port.in.CalculateBookingUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService implements CalculateBookingUseCase {

  private final RulesService rulesService;

  @Override
  public Booking calculateBooking(Accommodation accommodation) {
    throw new IllegalStateException("Not implemented");
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
