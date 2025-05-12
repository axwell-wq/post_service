package faang.school.postservice.amazons3;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3BucketOperationService {

    @Value("${s3.bucket_name}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    public String  uploadAvatar(MultipartFile file, Long userId) {
        S3Client s3Client = amazonS3Client.getS3Client();

        try {
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String key = "avatars/user_" + userId + "/" + UUID.randomUUID() + fileExtension;

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            return s3Client.utilities().getUrl(
                    builder -> builder.bucket(bucketName).key(key)).toString();
        } catch (S3Exception e) {
            log.error("Amazon S3 error: {}", e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to upload file to S3", e);
        } catch (IOException e) {
            log.error("File processing error", e);
            throw new RuntimeException("Failed to process file", e);
        } finally {
            s3Client.close();
        }
    }

    @PostConstruct
    public void createBucketIfNotExists() {
        try (S3Client s3Client = amazonS3Client.getS3Client()) {
            s3Client.createBucket(builder -> builder.bucket(bucketName));
            log.info("Bucket {} created successfully", bucketName);
        } catch (S3Exception e) {
            if (e.awsErrorDetails().errorCode().equals("BucketAlreadyExists")) {
                log.info("Bucket {} already exists", bucketName);
            } else {
                throw new RuntimeException("Failed to create bucket", e);
            }
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
