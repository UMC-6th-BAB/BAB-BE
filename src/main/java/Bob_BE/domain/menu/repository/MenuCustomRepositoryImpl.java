package Bob_BE.domain.menu.repository;

import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.menu.entity.QMenu;
import Bob_BE.domain.store.entity.Store;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class MenuCustomRepositoryImpl implements MenuCustomRepository {

    private final JPAQueryFactory queryFactory;

    public MenuCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Menu GetMaxPriceMenuByStore(Store store) {

        QMenu menu = QMenu.menu;

        return queryFactory
                .selectFrom(menu)
                .where(menu.store.eq(store))
                .orderBy(menu.price.desc())
                .limit(1)
                .fetchOne();
    }
}
