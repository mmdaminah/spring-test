package ir.mmdaminah.newtest.file;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import ir.mmdaminah.newtest.file.dto.validation.UploadBucketNames;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BucketCreator implements CommandLineRunner {

    private final MinioClient minioClient;
    private final UploadBucketNames bucketNames;

    @Override
    public void run(String... args) throws Exception {

        for (String bucket : bucketNames.getBucketNames()) {
            boolean bucketFound = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucket).build()
            );
            if (!bucketFound) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucket)
                                .build()
                );
            }
        }

    }
}
