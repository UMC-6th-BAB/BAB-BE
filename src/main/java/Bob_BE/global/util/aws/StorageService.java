package Bob_BE.global.util.aws;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String uploadFile(MultipartFile file, String dirName) throws IOException;
}
