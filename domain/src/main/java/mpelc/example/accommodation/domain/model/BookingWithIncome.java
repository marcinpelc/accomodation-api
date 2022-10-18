package mpelc.example.accommodation.domain.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingWithIncome {
  private Integer bookedEconomy;
  private Integer bookedPremium;
  private BigDecimal incomeEconomy;
  private BigDecimal incomePremium;
}
