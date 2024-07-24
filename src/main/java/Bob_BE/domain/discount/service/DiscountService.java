package Bob_BE.domain.discount.service;

import Bob_BE.domain.discount.dto.parameter.DiscountParameterDto;
import Bob_BE.domain.discount.entity.Discount;
import jakarta.validation.Valid;

import java.util.List;

public interface DiscountService {

    public Discount CreateDiscount(@Valid DiscountParameterDto.CreateDiscountParamDto param);
    public void DeleteDiscount(@Valid DiscountParameterDto.DeleteDiscountParamDto param);
    public void ChangeDiscountProgress();
    public List<Discount> GetDiscountedList(@Valid DiscountParameterDto.GetDiscountedListParamDto param);
}
