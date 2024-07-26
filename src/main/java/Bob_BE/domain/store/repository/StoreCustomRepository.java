package Bob_BE.domain.store.repository;

import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.store.entity.Store;

import java.util.List;

public interface StoreCustomRepository {

    List<StoreResponseDto.GetOnSaleStoreDataDto> GetOnSaleStoreAndMenuData(List<Store> storeList);
}
