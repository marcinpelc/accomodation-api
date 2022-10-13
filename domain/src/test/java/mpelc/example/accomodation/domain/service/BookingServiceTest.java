package mpelc.example.accomodation.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import mpelc.example.accomodation.domain.model.RankedGuests;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

  @Mock private RulesService rulesService;

  @InjectMocks private BookingService bookingService;

  @Test
  @Disabled
  void calculateBookingTest() {
    // TODO: implement tests
  }

  @Test
  void rankGuestsTest() {
    // given
    when(rulesService.getPremiumPricePredicate()).thenReturn(v -> v >= 100);
    List<BigDecimal> guestList = createDefaultGuestList();
    // when
    RankedGuests result = bookingService.rankGuests(guestList);
    // then
    assertEquals(4, result.getEconomy());
    assertEquals(6, result.getPremium());
  }

  private List<BigDecimal> createDefaultGuestList() {
    return Stream.of("23", "45", "155", "374", "22", "99.99", "100", "101", "115", "209")
        .map(BigDecimal::new)
        .collect(Collectors.toList());
  }
}
