package Bob_BE.domain.operatingHours.entity;

import Bob_BE.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Entity
@Table(name = "operation_hours")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperatingHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false)
    private DayOfWeek day;

    @Column(nullable = false)
    private Time open_time;

    @Column(nullable = false)
    private Time end_time;

    private Time break_start_time;

    private Time break_end_time;

    private boolean holiday;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

}
