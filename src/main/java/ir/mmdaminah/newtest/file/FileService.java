package ir.mmdaminah.newtest.file;

import io.minio.*;
import io.minio.errors.*;
import ir.mmdaminah.newtest.file.dto.response.FileUploadResponse;
import ir.mmdaminah.newtest.file.exception.BucketNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioClient minioClient;
    private final FileRepository fileRepository;


    public FileUploadResponse uploadFile(MultipartFile file, String bucketName) {
        try {

            String fileHash = calculateFileHash(file);

            Optional<File> foundFile = fileRepository.findByContentHash(fileHash);
            if (foundFile.isPresent()) {
                if(bucketName.equals(foundFile.get().getBucket())) {
                    return new FileUploadResponse("/%s/%s".formatted(
                            foundFile.get().getBucket(),
                            foundFile.get().getObjectName())
                    );
                }
            }

            boolean bucketFound = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketFound) {
                throw new BucketNotFound("bucket not found");
            }

            String fileName = UUID.randomUUID().toString();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            var fileMetadata = new File();
            fileMetadata.setObjectName(fileName);
            fileMetadata.setContentType(file.getContentType());
            fileMetadata.setSize(file.getSize());
            fileMetadata.setOriginalFileName(file.getOriginalFilename());
            fileMetadata.setBucket(bucketName);
            fileMetadata.setContentHash(fileHash);
            fileRepository.save(fileMetadata);

            return new FileUploadResponse("/%s/%s".formatted(bucketName, fileName));

        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream downloadFile(String bucketName, String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }


    }

    private String calculateFileHash(MultipartFile file) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(file.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}
