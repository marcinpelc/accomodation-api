package mpelc.example.accommodation.inbound.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import mpelc.example.accommodation.domain.model.Accommodation;
import mpelc.example.accommodation.domain.model.BookingWithIncome;
import mpelc.example.accommodation.inbound.payload.AccommodationRequestPayload;
import mpelc.example.accommodation.inbound.payload.AccommodationResponsePayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccommodationMapperTest {

  public static final int ECONOMY = 3;
  public static final int PREMIUM = 2;
  public static final BigDecimal INCOME = BigDecimal.valueOf(250);
  private AccommodationMapper accommodationMapper;

  @BeforeEach
  public void setUp() {
    accommodationMapper = new AccommodationMapper();
  }

  @Test
  public void toAccommodationTest() {
    AccommodationRequestPayload accommodationRequestPayload =
        createDefaultAccommodationRequestPayload();
    Accommodation accommodation = accommodationMapper.toAccommodation(accommodationRequestPayload);
    assertEquals(ECONOMY, accommodation.getEconomy());
    assertEquals(PREMIUM, accommodation.getPremium());
  }

  @Test
  public void toAccommodationResponsePayloadTest() {
    BookingWithIncome bookingWithIncome = createDefaultBookingWithIncome();
    AccommodationResponsePayload payload =
        accommodationMapper.toAccommodationResponsePayload(bookingWithIncome);
    assertEquals(INCOME, payload.getIncome());
    assertEquals(ECONOMY, payload.getBookedEconomy());
    assertEquals(PREMIUM, payload.getBookedPremium());
  }

  private BookingWithIncome createDefaultBookingWithIncome() {
    return BookingWithIncome.builder()
        .income(INCOME)
        .bookedEconomy(ECONOMY)
        .bookedPremium(PREMIUM)
        .build();
  }

  private AccommodationRequestPayload createDefaultAccommodationRequestPayload() {
    return AccommodationRequestPayload.builder().economy(ECONOMY).premium(PREMIUM).build();
  }
}
