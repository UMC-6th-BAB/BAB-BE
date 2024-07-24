package Bob_BE.domain.discount.service;

import Bob_BE.domain.discount.dto.parameter.DiscountParameterDto;
import Bob_BE.domain.discount.entity.Discount;
import jakarta.validation.Valid;

public interface DiscountService {

    public Discount CreateDiscount(@Valid DiscountParameterDto.CreateDiscountParamDto param);
}
