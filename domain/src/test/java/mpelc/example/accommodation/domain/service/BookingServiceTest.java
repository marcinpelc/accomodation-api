package mpelc.example.accommodation.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import mpelc.example.accommodation.domain.model.Accommodation;
import mpelc.example.accommodation.domain.model.Booking;
import mpelc.example.accommodation.domain.model.BookingWithIncome;
import mpelc.example.accommodation.domain.model.RankedGuests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

  private static final Predicate<BigDecimal> DEFAULT_PREDICATE =
      v -> v.compareTo(BigDecimal.valueOf(100)) >= 0;
  public static final int BOOKED_ECONOMY = 2;
  public static final int BOOKED_PREMIUM = 3;
  public static final int BOOKED_PREMIUM_BY_ECONOMY = 1;

  @Mock private RulesService rulesService;

  @InjectMocks private BookingService bookingService;

  @Test
  public void calculateBookingWithIncomeTest() {
    when(rulesService.getPremiumPricePredicate()).thenReturn(DEFAULT_PREDICATE);
    Accommodation accommodation = createAccommodation(2, 8);
    BookingWithIncome bookingWithIncome = bookingService.calculateBookingWithIncome(accommodation);
    assertEquals(1243.99, bookingWithIncome.getIncome().doubleValue());
    assertEquals(2, bookingWithIncome.getBookedEconomy());
    assertEquals(8, bookingWithIncome.getBookedPremium());
  }

  @Test
  public void mapToBookingWithIncomeTest() {
    BigDecimal income = BigDecimal.ONE;
    Booking booking =
        Booking.builder()
            .bookedEconomy(BOOKED_ECONOMY)
            .bookedPremium(BOOKED_PREMIUM)
            .bookedPremiumByEconomy(BOOKED_PREMIUM_BY_ECONOMY)
            .build();
    BookingWithIncome bookingWithIncome = bookingService.mapToBookingWithIncome(income, booking);
    assertEquals(BigDecimal.ONE, bookingWithIncome.getIncome());
    assertEquals(BOOKED_ECONOMY, bookingWithIncome.getBookedEconomy());
    assertEquals(BOOKED_PREMIUM + BOOKED_PREMIUM_BY_ECONOMY, bookingWithIncome.getBookedPremium());
  }

  @Test
  public void calculateIncomeTest() {
    Booking booking = createBooking(1, 1, 0);
    List<BigDecimal> guestList = createDefaultGuestList();
    BigDecimal income = bookingService.calculateIncome(booking, guestList, DEFAULT_PREDICATE);
    assertEquals(178, income.doubleValue());
  }

  @Test
  public void calculateIncomeWhenAllBookedTest() {
    Booking booking = createBooking(4, 6, 0);
    List<BigDecimal> guestList = createDefaultGuestList();
    BigDecimal income = bookingService.calculateIncome(booking, guestList, DEFAULT_PREDICATE);
    assertEquals(1243.99, income.doubleValue());
  }

  @Test
  public void calculateIncomeWhenAllBookedButTwoEconomyGuestsInPremiumTest() {
    Booking booking = createBooking(2, 6, 2);
    List<BigDecimal> guestList = createDefaultGuestList();
    BigDecimal income = bookingService.calculateIncome(booking, guestList, DEFAULT_PREDICATE);
    assertEquals(1243.99, income.doubleValue());
  }

  @Test
  public void calculateIncomeWhenOnly4PremiumBookedTest() {
    Booking booking = createBooking(0, 4, 0);
    List<BigDecimal> guestList = createDefaultGuestList();
    BigDecimal income = bookingService.calculateIncome(booking, guestList, DEFAULT_PREDICATE);
    assertEquals(730, income.doubleValue());
  }

  @Test
  public void calculateIncomeWhenOnly3EconomyBookedTest() {
    Booking booking = createBooking(3, 0, 0);
    List<BigDecimal> guestList = createDefaultGuestList();
    BigDecimal income = bookingService.calculateIncome(booking, guestList, DEFAULT_PREDICATE);
    assertEquals(90, income.doubleValue());
  }

  @Test
  public void calculateIncomeWhenNoneBookedTest() {
    Booking booking = createBooking(0, 0, 0);
    List<BigDecimal> guestList = createDefaultGuestList();
    BigDecimal income = bookingService.calculateIncome(booking, guestList, DEFAULT_PREDICATE);
    assertEquals(0, income.doubleValue());
  }

  @Test
  public void calculateBookingTest_GivenOneEconomyGuestOneEconomyRoom() {
    // given
    RankedGuests rankedGuests = createRankedGuests(1, 0);
    Accommodation accommodation = createAccommodation(1, 0);
    // when
    Booking result = bookingService.calculateBooking(accommodation, rankedGuests);
    // then
    assertEquals(1, result.getBookedEconomy());
    assertEquals(0, result.getBookedPremium());
    assertEquals(0, result.getBookedPremiumByEconomy());
  }

  @Test
  public void calculateBookingTest_GivenOneEconomyGuestOnePremiumRoom() {
    // given
    RankedGuests rankedGuests = createRankedGuests(1, 0);
    Accommodation accommodation = createAccommodation(0, 1);
    // when
    Booking result = bookingService.calculateBooking(accommodation, rankedGuests);
    // then
    assertEquals(0, result.getBookedEconomy());
    assertEquals(0, result.getBookedPremium());
    assertEquals(1, result.getBookedPremiumByEconomy());
  }

  @Test
  public void calculateBookingTest_GivenTwoPremiumGuestOnePremiumRoom() {
    // given
    RankedGuests rankedGuests = createRankedGuests(0, 2);
    Accommodation accommodation = createAccommodation(1, 1);
    // when
    Booking result = bookingService.calculateBooking(accommodation, rankedGuests);
    // then
    assertEquals(0, result.getBookedEconomy());
    assertEquals(1, result.getBookedPremium());
    assertEquals(0, result.getBookedPremiumByEconomy());
  }

  @Test
  public void calculateBookingTest_GivenThreeEconomyGuestOnePremiumRoomOneEconomyRoom() {
    // given
    RankedGuests rankedGuests = createRankedGuests(3, 0);
    Accommodation accommodation = createAccommodation(1, 1);
    // when
    Booking result = bookingService.calculateBooking(accommodation, rankedGuests);
    // then
    assertEquals(1, result.getBookedEconomy());
    assertEquals(0, result.getBookedPremium());
    assertEquals(1, result.getBookedPremiumByEconomy());
  }

  @Test
  public void
      calculateBookingTest_GivenThreeEconomyGuestOnePremiumGuestOnePremiumRoomOneEconomyRoom() {
    // given
    RankedGuests rankedGuests = createRankedGuests(3, 1);
    Accommodation accommodation = createAccommodation(1, 1);
    // when
    Booking result = bookingService.calculateBooking(accommodation, rankedGuests);
    // then
    assertEquals(1, result.getBookedEconomy());
    assertEquals(1, result.getBookedPremium());
    assertEquals(0, result.getBookedPremiumByEconomy());
  }

  @Test
  public void
      calculateBookingTest_GivenThreeEconomyGuestOnePremiumGuestTwoPremiumRoomOneEconomyRoom() {
    // given
    RankedGuests rankedGuests = createRankedGuests(3, 1);
    Accommodation accommodation = createAccommodation(1, 2);
    // when
    Booking result = bookingService.calculateBooking(accommodation, rankedGuests);
    // then
    assertEquals(1, result.getBookedEconomy());
    assertEquals(1, result.getBookedPremium());
    assertEquals(1, result.getBookedPremiumByEconomy());
  }

  @Test
  public void rankGuestsTest() {
    // given
    when(rulesService.getPremiumPricePredicate()).thenReturn(DEFAULT_PREDICATE);
    List<BigDecimal> guestList = createDefaultGuestList();
    // when
    RankedGuests result = bookingService.rankGuests(guestList);
    // then
    assertEquals(4, result.getEconomy());
    assertEquals(6, result.getPremium());
  }

  private Booking createBooking(int economy, int premium, int economyInPremium) {
    return Booking.builder()
        .bookedEconomy(economy)
        .bookedPremium(premium)
        .bookedPremiumByEconomy(economyInPremium)
        .build();
  }

  private Accommodation createAccommodation(int economy, int premium) {
    return Accommodation.builder().economy(economy).premium(premium).build();
  }

  private RankedGuests createRankedGuests(int economy, int premium) {
    return RankedGuests.builder().economy(economy).premium(premium).build();
  }

  private List<BigDecimal> createDefaultGuestList() {
    return Stream.of("23", "45", "155", "374", "22", "99.99", "100", "101", "115", "209")
        .map(BigDecimal::new)
        .collect(Collectors.toList());
  }
}
