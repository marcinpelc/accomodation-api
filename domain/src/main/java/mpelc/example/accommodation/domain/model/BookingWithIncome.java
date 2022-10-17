package mpelc.example.accommodation.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BookingWithIncome {
  private Integer bookedEconomy;
  private Integer bookedPremium;
  private BigDecimal income;
}
