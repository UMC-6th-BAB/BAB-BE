package Bob_BE.domain.signatureMenu.repository;

import Bob_BE.domain.signatureMenu.entity.SignatureMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignatureMenuRepository extends JpaRepository<SignatureMenu, Long> {
}
