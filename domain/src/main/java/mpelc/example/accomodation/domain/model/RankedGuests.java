package mpelc.example.accomodation.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RankedGuests {
  private Integer economy;
  private Integer premium;
}
