package Bob_BE.domain.owner.repository;

import Bob_BE.domain.owner.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findBySocialId(Long socialId);
}
