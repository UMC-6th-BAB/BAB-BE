package Bob_BE.global.util.google;

import com.google.cloud.vision.v1.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleCloudOCRService {

    @Value("${cloud.google.storage.bucket.filePath}")
    String bucketFilePath;
    private final GoogleCloudStorageService googleCloudStorageService;

    public List<Map.Entry<String, Integer>> detectTextGcs(MultipartFile cardImg) throws IOException {
        googleCloudStorageService.upload(cardImg);

        // TODO(developer): Replace these variables before running the sample.
        String filePath = bucketFilePath + cardImg.getOriginalFilename();
        log.info("filePath: {}", filePath);
        return detectTextGcs(filePath);
    }

    // Detects text in the specified remote image on Google Cloud Storage.
    public List<Map.Entry<String, Integer>> detectTextGcs(String gcsPath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(gcsPath).build();
        log.info("GcsImageUri: {}", imgSource.getGcsImageUri());
        log.info("imgSource.getImageUri: {}", imgSource.getImageUri());
        log.info("imgSource.getDefaultInstanceForType(): {}", imgSource.getDefaultInstanceForType());
        Image img = Image.newBuilder().setSource(imgSource).build();
        log.info("img.getContent(): {}", img.getContent());
        log.info("img.getContent(): {}", img.getSource());

        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            List<Map.Entry<String, Integer>> result = new ArrayList<>();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return null;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    System.out.format("Text: %s%n", annotation.getDescription());
                    List<Vertex> vertices = annotation.getBoundingPoly().getVerticesList();

                    int averageY = getAverageVertices(vertices);

                    System.out.format("Position : %s%n", vertices);

                    result.add(new AbstractMap.SimpleEntry<>(annotation.getDescription(), averageY));
                }
            }
            return result;
        }
    }

    public Integer getAverageVertices(List<Vertex> vertices){
        int averageY = 0;
        for (Vertex v : vertices){
            averageY =+  v.getY();
        }
        return averageY/4;
    }
}
