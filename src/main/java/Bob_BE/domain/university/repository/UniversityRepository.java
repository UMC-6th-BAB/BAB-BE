package Bob_BE.domain.university.repository;

import Bob_BE.domain.university.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UniversityRepository extends JpaRepository<University, Long> {
    Optional<University> findByUniversityName(String universityName);
}
