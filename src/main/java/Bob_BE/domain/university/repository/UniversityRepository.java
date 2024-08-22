package Bob_BE.domain.university.repository;

import Bob_BE.domain.university.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UniversityRepository extends JpaRepository<University, Long> {

    Optional<University> findByUniversityName(String universityName);

    @Query("SELECT uni FROM University uni WHERE uni.universityName LIKE %:universityName%")
    List<University> findAllByUniversityName(String universityName);
}
