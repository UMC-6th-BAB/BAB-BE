package Bob_BE.domain.store.repository;

import Bob_BE.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreCustomRepository {

}
