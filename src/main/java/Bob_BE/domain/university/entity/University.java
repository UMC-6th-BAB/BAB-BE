package Bob_BE.domain.university.entity;

import Bob_BE.domain.storeUniversity.entity.StoreUniversity;
import Bob_BE.domain.student.entity.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "university")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class University{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<Student> studentList = new ArrayList<>();

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<StoreUniversity> storeUniversityList = new ArrayList<>();
}
