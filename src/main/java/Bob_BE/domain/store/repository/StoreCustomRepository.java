package Bob_BE.domain.store.repository;

import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.university.entity.University;

import java.util.List;

public interface StoreCustomRepository {

    List<StoreResponseDto.StoreAndDiscountDataDto> GetOnSaleStoreAndDiscount(University university);
    List<StoreResponseDto.GetOnSaleStoreDataDto> GetOnSaleMenuData(List<StoreResponseDto.GetOnSaleStoreDataDto> getOnSaleStoreDataDtoList);
}
