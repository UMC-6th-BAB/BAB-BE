package Bob_BE.domain.operatingHours.repository;

import Bob_BE.domain.operatingHours.entity.OperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperatingHoursRepository extends JpaRepository<OperatingHours, Long> {

    Optional<OperatingHours> findByStoreId(Long storeId);
}
