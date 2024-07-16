package Bob_BE.domain.store.entity;

import Bob_BE.domain.banner.entity.Banner;
import Bob_BE.domain.discount.entity.Discount;
import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.operatingHours.entity.OperatingHours;
import Bob_BE.domain.owner.entity.Owner;
import Bob_BE.domain.storeUniversity.entity.StoreUniversity;
import Bob_BE.domain.student.entity.Student;
import Bob_BE.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "store")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private String name;

    private String address;

    private String street_address;

    private String store_link;

    private String registration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Owner owner;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<StoreUniversity> storeUniversityList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Banner> bannerList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<OperatingHours> operatingHoursList = new ArrayList<>();
}
