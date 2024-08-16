package Bob_BE.global.util;

import Bob_BE.domain.store.service.StoreService;
import Bob_BE.domain.university.service.UniversityService;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    private final StoreService storeService;
    private final UniversityService universityService;

    public DataInitializer(StoreService storeService, UniversityService universityService) {
        this.storeService = storeService;
        this.universityService = universityService;
    }

    @PostConstruct
    public void initialize(){
        storeService.saveAllStoreLocationsToRedis();
        universityService.saveAllUniversityLocationsToRedis();
    }

}
