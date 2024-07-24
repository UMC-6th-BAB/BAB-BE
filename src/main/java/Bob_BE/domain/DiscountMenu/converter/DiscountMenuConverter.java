package Bob_BE.domain.DiscountMenu.converter;

import Bob_BE.domain.DiscountMenu.entity.DiscountMenu;
import Bob_BE.domain.discount.dto.data.DiscountDataDto;
import Bob_BE.domain.discount.entity.Discount;
import Bob_BE.domain.menu.entity.Menu;

import java.util.List;

public class DiscountMenuConverter {

    public static DiscountMenu toDiscountMenu (Menu menu, Integer discountPrice) {

        return DiscountMenu.builder()
                .discountPrice(discountPrice)
                .menu(menu)
                .build();
    }
}
