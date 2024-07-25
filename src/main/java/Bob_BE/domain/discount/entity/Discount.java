package Bob_BE.domain.discount.entity;

import Bob_BE.domain.discountMenu.entity.DiscountMenu;
import Bob_BE.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "discount")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="discount_id")
    private Long id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private String title;

    private boolean inProgress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL)
    private List<DiscountMenu> discountMenuList = new ArrayList<>();

    /**
     * 상태 수정 로직
     */
    public void setInProgress() {

        LocalDate nowDate = LocalDate.now();
        if (nowDate.compareTo(this.startDate) >= 0 && nowDate.compareTo(this.endDate) <= 0) {
            this.inProgress = true;
        }
        else this.inProgress = false;
    }
}
