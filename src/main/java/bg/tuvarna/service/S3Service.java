package bg.tuvarna.service;

import jakarta.ws.rs.core.Response;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.File;
import java.io.IOException;

public interface S3Service {
    void uploadToS3(File file, String keyName) throws IOException;

    Response download(String fileName);

    ResponseInputStream<GetObjectResponse> getFile(String fileName);

    DeleteObjectResponse deleteFile(String fileName);

    void uploadThumbnail(byte[] bytes, String key);
}
