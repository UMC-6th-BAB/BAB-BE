package Bob_BE.domain.storeUniversity.repository;

import Bob_BE.domain.storeUniversity.entity.StoreUniversity;
import Bob_BE.domain.university.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreUniversityRepository extends JpaRepository<StoreUniversity, Long> {

    Optional<StoreUniversity> findByStoreId(Long storeId);
    Optional<List<StoreUniversity>> findAllByUniversity(University university);
}
