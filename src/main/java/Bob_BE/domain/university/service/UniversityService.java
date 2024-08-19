package Bob_BE.domain.university.service;

import Bob_BE.domain.university.entity.University;
import Bob_BE.domain.university.repository.UniversityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UniversityService {
    private final UniversityRepository universityRepository;

    public List<University> getUniversityByUniversityName(String universityName) {
        return universityRepository.findAllByUniversityName(universityName);
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveAllUniversityLocationsToRedis(){
        List<University> universities = universityRepository.findAll();
        String key = "locations";

        for (University university : universities){
            redisTemplate.opsForGeo().add(key, new Point(university.getLongitude(), university.getLatitude()), "university:"+university.getId());
        }
    }
}
