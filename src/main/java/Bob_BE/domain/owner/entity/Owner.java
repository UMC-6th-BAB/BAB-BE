package Bob_BE.domain.owner.entity;

import Bob_BE.domain.store.entity.Store;
import Bob_BE.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "owner")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Owner extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="owner_id")
    private Long id;

    private String nickname;

    private String email;

    @Column(nullable = false)
    private Long socialId;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Store> storeList = new ArrayList<>();
}
