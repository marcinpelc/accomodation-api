package mpelc.example.accomodation.domain.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Accommodation {
  private Integer economy;
  private Integer premium;
}
