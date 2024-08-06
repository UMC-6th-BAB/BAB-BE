package Bob_BE.domain.discount.repository;

import Bob_BE.domain.discount.entity.Discount;
import Bob_BE.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Optional<List<Discount>> findAllByStoreAndInProgress(Store store, Boolean inProgress);
}
