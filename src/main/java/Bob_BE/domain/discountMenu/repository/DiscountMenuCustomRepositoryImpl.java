package Bob_BE.domain.discountMenu.repository;

import Bob_BE.domain.discountMenu.entity.DiscountMenu;
import Bob_BE.domain.discountMenu.entity.QDiscountMenu;
import Bob_BE.domain.store.entity.Store;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class DiscountMenuCustomRepositoryImpl implements DiscountMenuCustomRepository {

    private final JPAQueryFactory queryFactory;

    public DiscountMenuCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public DiscountMenu GetDiscountMenuByStoreAndMaxDiscountPrice(Store store) {

        QDiscountMenu discountMenu = QDiscountMenu.discountMenu;

        return queryFactory
                .selectFrom(discountMenu)
                .where(discountMenu.menu.store.eq(store)
                        .and(discountMenu.discount.inProgress.isTrue()))
                .orderBy(discountMenu.discountPrice.desc())
                .limit(1)
                .fetchOne();
    }
}
