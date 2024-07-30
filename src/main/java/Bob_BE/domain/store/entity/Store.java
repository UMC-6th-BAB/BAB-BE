package Bob_BE.domain.store.entity;

import Bob_BE.domain.banner.entity.Banner;
import Bob_BE.domain.discount.entity.Discount;
import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.operatingHours.entity.OperatingHours;
import Bob_BE.domain.owner.entity.Owner;
import Bob_BE.domain.store.dto.request.StoreRequestDto;
import Bob_BE.domain.storeUniversity.entity.StoreUniversity;
import Bob_BE.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "store")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@SQLDelete(sql = "UPDATE store SET deleted_at=current_timestamp(6) WHERE store_id = ?")
@Where(clause = "deleted_at is null")
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="store_id")
    private Long id;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private String name;

    private String address;

    private String streetAddress;

    private String storeLink;

    private String registration;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<StoreUniversity> storeUniversityList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Banner> bannerList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Menu> menuList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<OperatingHours> operatingHoursList = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<Discount> discountList = new ArrayList<>();

    public void updateStore(StoreRequestDto.StoreUpdateRequestDto requestDto){
        this.name=requestDto.getName();
        this.longitude=requestDto.getLongitude();
        this.latitude=requestDto.getLatitude();
        this.address=requestDto.getAddress();
        this.streetAddress=requestDto.getStreetAddress();
        this.storeLink=requestDto.getStoreLink();
        this.registration=requestDto.getRegistration();
    }

}
