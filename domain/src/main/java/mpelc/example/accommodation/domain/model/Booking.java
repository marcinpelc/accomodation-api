package mpelc.example.accommodation.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Booking {
  private Integer bookedEconomy;
  private Integer bookedPremium;
  private Integer bookedPremiumByEconomy;
}
