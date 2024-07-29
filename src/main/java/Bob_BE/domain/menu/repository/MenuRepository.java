package Bob_BE.domain.menu.repository;

import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Optional<List<Menu>> findAllByStore(Store store);
}
