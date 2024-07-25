package Bob_BE.domain.menu.entity;

import Bob_BE.domain.discountMenu.entity.DiscountMenu;
import Bob_BE.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="menu_id")
    private Long id;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String menuUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<DiscountMenu> discountMenuList = new ArrayList<>();

}
