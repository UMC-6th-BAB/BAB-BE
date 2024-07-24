package Bob_BE.domain.university.service;

import Bob_BE.domain.university.entity.University;
import Bob_BE.domain.university.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UniversityService {
    private final UniversityRepository universityRepository;

    public University findOrCreateUniversity(String universityName, String universityAddress) {
        Optional<University> university = universityRepository.findByUniversityNameAndAddress(universityName, universityAddress);
        if (university.isPresent()) {
            return university.get();
        } else {
            University newUniversity = University
                    .builder()
                    .universityName(universityName)
                    .address(universityAddress)
                    .build();
            return universityRepository.save(newUniversity);
        }
    }
}
