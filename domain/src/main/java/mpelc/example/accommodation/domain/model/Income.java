package mpelc.example.accommodation.domain.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Income {
  BigDecimal economy;
  BigDecimal premium;
}
