package Bob_BE.domain.discount.repository;

import Bob_BE.domain.discount.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
