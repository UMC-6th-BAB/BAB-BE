package Bob_BE.domain.menu.repository;

import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.store.entity.Store;

public interface MenuCustomRepository {

    Menu GetMaxPriceMenuByStore(Store store);
}
