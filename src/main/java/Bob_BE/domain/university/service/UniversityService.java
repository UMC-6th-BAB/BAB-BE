package Bob_BE.domain.university.service;

import Bob_BE.domain.university.entity.University;
import Bob_BE.domain.university.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UniversityService {
    public final UniversityRepository universityRepository;

    public List<University> getUniversityByUniversityName(String universityName) {

        return universityRepository.findAllByUniversityName(universityName);

    }


}
