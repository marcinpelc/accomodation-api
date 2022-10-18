package mpelc.example.accommodation.inbound.payload;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccommodationRequestPayload {
  private Integer economy;
  private Integer premium;
}
