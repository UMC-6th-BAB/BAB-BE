package Bob_BE.domain.store.repository;

import Bob_BE.domain.discount.entity.QDiscount;
import Bob_BE.domain.discountMenu.entity.QDiscountMenu;
import Bob_BE.domain.menu.entity.QMenu;
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.store.entity.QStore;
import Bob_BE.domain.store.entity.Store;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StoreCustomRepositoryImpl implements StoreCustomRepository {

    private final JPAQueryFactory queryFactory;

    public StoreCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<StoreResponseDto.GetOnSaleStoreDataDto> GetOnSaleStoreAndMenuData(List<Store> storeList) {

        QStore store = QStore.store;
        QDiscount discount = QDiscount.discount;
        QDiscountMenu discountMenu = QDiscountMenu.discountMenu;
        QMenu menu = QMenu.menu;

        /*return queryFactory
                .select(Projections.constructor(StoreResponseDto.GetOnSaleStoreDataDto.class,
                        store.id,
                        discount.title,
                        store.name,
                        Projections.list(Projections.constructor(StoreResponseDto.GetOnSaleStoreMenuDataDto.class,
                                menu.menuName,
                                menu.price,
                                discountMenu.discountPrice))
                        ))
                .from(store)*/
        return null;
    }
}
