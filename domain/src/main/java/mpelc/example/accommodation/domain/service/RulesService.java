package mpelc.example.accommodation.domain.service;

import java.math.BigDecimal;
import java.util.function.Predicate;
import org.springframework.stereotype.Service;

@Service
public class RulesService {

  Predicate<BigDecimal> getPremiumPricePredicate() {
    return v -> v.compareTo(BigDecimal.valueOf(100)) >= 0;
  }
}
