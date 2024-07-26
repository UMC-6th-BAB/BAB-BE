package Bob_BE.domain.store.entity;

import Bob_BE.domain.banner.entity.Banner;
import Bob_BE.domain.menu.entity.Menu;
import Bob_BE.domain.operatingHours.entity.OperatingHours;
import Bob_BE.domain.owner.entity.Owner;
import Bob_BE.domain.store.dto.request.StoreRequestDto;
import Bob_BE.domain.storeUniversity.entity.StoreUniversity;
import Bob_BE.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
