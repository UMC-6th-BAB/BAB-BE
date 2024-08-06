package Bob_BE.domain.banner.repository;

import Bob_BE.domain.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {
}
