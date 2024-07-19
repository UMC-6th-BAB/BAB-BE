package Bob_BE.domain.student.repository;

import Bob_BE.domain.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findBySocialId(Long socialId);
}
