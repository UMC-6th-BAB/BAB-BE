package Bob_BE.domain.discountMenu.repository;

import Bob_BE.domain.discountMenu.entity.DiscountMenu;
import Bob_BE.domain.store.entity.Store;

public interface DiscountMenuCustomRepository {

    DiscountMenu GetDiscountMenuByStoreAndMaxDiscountPrice(Store store);
}
