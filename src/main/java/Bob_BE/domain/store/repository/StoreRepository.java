package Bob_BE.domain.store.repository;

import Bob_BE.domain.owner.entity.Owner;
import Bob_BE.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreCustomRepository {
    Optional<Store> findFirstByOwnerId(Long ownerId);

    @Query("SELECT DISTINCT s FROM Store s JOIN s.menuList m JOIN s.storeUniversityList su WHERE m.menuName LIKE %:keyword% AND su.university.id = :universityId")
    List<Store> findStoresByMenuKeyword(String keyword, Long universityId);

    @Query("SELECT DISTINCT s FROM Store s JOIN s.menuList m WHERE m.menuName LIKE %:keyword%")
    List<Store> findStoresByMenuKeywordAndCoordinates(String keyword);
}
