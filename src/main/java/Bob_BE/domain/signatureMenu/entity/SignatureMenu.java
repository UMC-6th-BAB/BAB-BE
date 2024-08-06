package Bob_BE.domain.signatureMenu.entity;

import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "signature_menu")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
public class SignatureMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "signature_menu_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
}
