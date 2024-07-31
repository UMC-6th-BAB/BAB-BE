package Bob_BE.domain.store.repository;

import Bob_BE.domain.discount.entity.QDiscount;
import Bob_BE.domain.discountMenu.entity.QDiscountMenu;
import Bob_BE.domain.menu.entity.QMenu;
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.store.entity.QStore;
import Bob_BE.domain.storeUniversity.entity.QStoreUniversity;
import Bob_BE.domain.university.entity.University;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class StoreCustomRepositoryImpl implements StoreCustomRepository {

    private final JPAQueryFactory queryFactory;

    public StoreCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<StoreResponseDto.StoreAndDiscountDataDto> GetOnSaleStoreAndDiscount(University university) {

        QStore store = QStore.store;
        QDiscount discount = QDiscount.discount;
        QDiscountMenu discountMenu = QDiscountMenu.discountMenu;
        QStoreUniversity storeUniversity = QStoreUniversity.storeUniversity;

        List<StoreResponseDto.StoreAndDiscountDataDto> results = queryFactory
                .select(Projections.constructor(StoreResponseDto.StoreAndDiscountDataDto.class,
                        store,
                        discount))
                .from(store)
                .leftJoin(store.discountList, discount)
                .leftJoin(discount.discountMenuList, discountMenu)
                .leftJoin(store.storeUniversityList, storeUniversity)
                .groupBy(store, discount)
                .orderBy(discountMenu.discountPrice.max().desc())
                .where(storeUniversity.university.eq(university)
                        .and(discount.inProgress.isTrue()))
                .fetch();

        return results;
    }

    @Override
    public List<StoreResponseDto.GetOnSaleStoreDataDto> GetOnSaleMenuData(List<StoreResponseDto.GetOnSaleStoreDataDto> getOnSaleStoreDataDtoList) {

        QDiscount discount = QDiscount.discount;
        QDiscountMenu discountMenu = QDiscountMenu.discountMenu;
        QMenu menu = QMenu.menu;

        getOnSaleStoreDataDtoList
                .forEach(getOnSaleStoreDataDto -> {

                    List<StoreResponseDto.GetOnSaleStoreMenuDataDto> getOnSaleStoreMenuDataDtoList = queryFactory
                            .select(Projections.constructor(StoreResponseDto.GetOnSaleStoreMenuDataDto.class,
                                    menu.menuName,
                                    menu.price,
                                    discountMenu.discountPrice))
                            .from(discount)
                            .leftJoin(discount.discountMenuList, discountMenu)
                            .leftJoin(discountMenu.menu, menu)
                            .where(discount.id.eq(getOnSaleStoreDataDto.getDiscountId()))
                            .orderBy(discountMenu.discountPrice.desc())
                            .limit(3)
                            .fetch();

                    getOnSaleStoreDataDto.setGetOnSaleStoreMenuDataDtoList(getOnSaleStoreMenuDataDtoList);
                });

        return getOnSaleStoreDataDtoList;
    }
}
