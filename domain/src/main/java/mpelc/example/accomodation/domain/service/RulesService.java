package mpelc.example.accomodation.domain.service;

import java.util.function.Predicate;
import org.springframework.stereotype.Service;

@Service
public class RulesService {

  Predicate<Integer> getPremiumPricePredicate() {
    return v -> v >= 100;
  }
}
