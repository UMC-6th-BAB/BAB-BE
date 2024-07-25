package Bob_BE.domain.discountMenu.entity;

import Bob_BE.domain.discount.entity.Discount;
import Bob_BE.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "discount_menu")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class DiscountMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="discount_menu_id")
    private Long id;

    @Column(nullable = false)
    private Integer discountPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private Discount discount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    /** 연관관계 편의 메서드 **/
    public void setDiscount(Discount discount) {

        if (this.discount != null) {
            this.discount.getDiscountMenuList().remove(this);
        }

        this.discount = discount;
        this.discount.getDiscountMenuList().add(this);
    }

    public void setMenu(Menu menu) {

        if (this.menu != null) {
            this.menu.getDiscountMenuList().remove(this);
        }

        this.menu = menu;
        this.menu.getDiscountMenuList().add(this);
    }
}
