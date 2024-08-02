package Bob_BE.domain.operatingHours.repository;

import Bob_BE.domain.operatingHours.dto.request.OHRequestDto;
import Bob_BE.domain.operatingHours.entity.DayOfWeek;
import Bob_BE.domain.operatingHours.entity.OperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OperatingHoursRepository extends JpaRepository<OperatingHours, Long> {

    Optional<List<OperatingHours>> findAllByStoreId(Long storeId);
}
