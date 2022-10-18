package mpelc.example.accommodation.inbound.payload;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccommodationResponsePayload {
  private Integer bookedEconomy;
  private Integer bookedPremium;
  private BigDecimal income;
}
