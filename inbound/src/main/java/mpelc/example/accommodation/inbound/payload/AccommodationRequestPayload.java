package mpelc.example.accommodation.inbound.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccommodationRequestPayload {
  private Integer economy;
  private Integer premium;
}
