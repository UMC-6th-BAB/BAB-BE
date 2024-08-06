package Bob_BE.domain.discountMenu.repository;

import Bob_BE.domain.discountMenu.entity.DiscountMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountMenuRepository extends JpaRepository<DiscountMenu, Long>, DiscountMenuCustomRepository {
}
