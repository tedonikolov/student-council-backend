package bg.tuvarna.service.impl;

import bg.tuvarna.service.S3Service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@ApplicationScoped
public class S3ServiceImpl implements S3Service {
    @Inject
    S3Client s3Client;

    private final String bucketName = "s3-bucket";

    public ResponseInputStream<GetObjectResponse> getFile(String fileName) {
        if (s3Client.listBuckets().buckets().stream().noneMatch(b -> b.name().equals(bucketName))) {
            return null;
        }
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        return s3Client.getObject(getObjectRequest);
    }

    public DeleteObjectResponse deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        return s3Client.deleteObject(deleteObjectRequest);
    }

    public void uploadToS3(File file, String keyName) throws IOException {
        if(!keyName.startsWith("videos/")) {
            keyName = "images/" + keyName;
        }

        try (InputStream inputStream = new FileInputStream(file)) {
            s3Client.putObject(
                    PutObjectRequest.builder().bucket(bucketName).key(keyName).build(),
                    RequestBody.fromInputStream(inputStream, file.length())
            );
        }
    }

    public void uploadThumbnail(byte[] thumbnailBytes, String key) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(thumbnailBytes));
    }

    public Response download(String fileName) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            InputStream inputStream = s3Client.getObject(getObjectRequest);

            String contentType = Files.probeContentType(Paths.get(fileName));
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return Response.ok(inputStream)
                    .type(contentType)
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .build();
        } catch (Exception e) {
            if (e.getClass().equals(NoSuchKeyException.class)) System.out.println(e.getMessage());
            else e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("File not found: " + e.getMessage()).build();
        }
    }
}
