package Bob_BE.global.util.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class S3StorageService implements StorageService{

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file, String dirName) throws IOException {
        String fileName = generateFileName(file, dirName);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, metadata);
            amazonS3Client.putObject(putObjectRequest);
        } catch (IOException e) {
            throw new IOException("Failed to upload file to S3", e);
        }
        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    private String generateFileName(MultipartFile file, String dirName){
        return dirName + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
    }
}
