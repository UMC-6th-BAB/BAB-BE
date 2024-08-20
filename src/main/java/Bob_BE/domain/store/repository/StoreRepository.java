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

    @Query("""
    SELECT DISTINCT s FROM Store s\s
    JOIN s.menuList m\s
    WHERE m.menuName LIKE %:keyword%\s
    AND (6371 * acos(cos(radians(:latitude)) * cos(radians(s.latitude)) * cos(radians(s.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(s.latitude)))) < :radius
""")
    List<Store> findStoresByMenuKeywordAndCoordinates(String keyword, double latitude, double longitude, double radius);

}
