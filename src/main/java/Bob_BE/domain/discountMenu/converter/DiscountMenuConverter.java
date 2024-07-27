package Bob_BE.domain.discountMenu.converter;

import Bob_BE.domain.discountMenu.entity.DiscountMenu;
import Bob_BE.domain.menu.entity.Menu;

public class DiscountMenuConverter {

    public static DiscountMenu toDiscountMenu (Menu menu, Integer discountPrice) {

        return DiscountMenu.builder()
                .discountPrice(discountPrice)
                .menu(menu)
                .build();
    }
}
