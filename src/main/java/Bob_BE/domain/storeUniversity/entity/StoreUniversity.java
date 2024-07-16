package Bob_BE.domain.storeUniversity.entity;

import Bob_BE.domain.store.entity.Store;
import Bob_BE.domain.university.entity.University;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store_university")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreUniversity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;
}
