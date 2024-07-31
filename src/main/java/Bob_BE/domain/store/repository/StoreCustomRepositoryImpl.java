package Bob_BE.domain.store.repository;

import Bob_BE.domain.discount.entity.QDiscount;
import Bob_BE.domain.discountMenu.entity.QDiscountMenu;
import Bob_BE.domain.menu.entity.QMenu;
import Bob_BE.domain.store.dto.response.StoreResponseDto;
import Bob_BE.domain.store.entity.QStore;
import Bob_BE.domain.store.entity.Store;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
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
    public List<Long> GetOnSaleStoreId(List<Store> storeList) {

        QStore store = QStore.store;
        QDiscount discount = QDiscount.discount;
        QDiscountMenu discountMenu = QDiscountMenu.discountMenu;
        QMenu menu = QMenu.menu;

        JPQLQuery<Integer> maxDiscountPriceSubQuery = JPAExpressions
                .select(discountMenu.discountPrice.max())
                .from(discountMenu)
                .where(discountMenu.discount.eq(discount));

        List<Tuple> results = queryFactory
                .select(store.id, discountMenu.discountPrice)
                .from(store)
                .leftJoin(store.discountList, discount)
                .leftJoin(discount.discountMenuList, discountMenu)
                .groupBy(store.id)
                .where(discountMenu.discountPrice.eq(maxDiscountPriceSubQuery))
                .orderBy(discountMenu.discountPrice.desc())
                .fetch();

        return results.stream()
                .map(result -> result.get(store.id))
                .collect(Collectors.toList());
    }
}
