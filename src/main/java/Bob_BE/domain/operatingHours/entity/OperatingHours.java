package Bob_BE.domain.operatingHours.entity;

import Bob_BE.domain.operatingHours.dto.request.OHRequestDto;
import Bob_BE.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Time;

@Entity
@Table(name = "operating_hours")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class OperatingHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="operating_hours_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;
    
    private Time openTime;

    private Time closeTime;

    private Time breakStartTime;

    private Time breakEndTime;

    private Boolean holiday;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public void updateOH(OHRequestDto requestDto){
        this.openTime=requestDto.getOpenTime();
        this.closeTime=requestDto.getCloseTime();
        this.breakStartTime=requestDto.getBreakTime().getStartTime();
        this.breakEndTime=requestDto.getBreakTime().getEndTime();
        this.holiday=false;
    }

    public void updateHoliday(){
        this.openTime=null;
        this.closeTime=null;
        this.breakStartTime=null;
        this.breakEndTime=null;
        this.holiday=true;
    }
}
