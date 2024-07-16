package Bob_BE.domain.owner.entity;

import Bob_BE.domain.store.entity.Store;
import Bob_BE.global.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "owner")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Owner extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    private String nickname;

    private String email;

    @Column(nullable = false)
    private String socialId;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Store> storeList = new ArrayList<>();
}
