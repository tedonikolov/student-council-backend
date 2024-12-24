package bg.tuvarna.service.producers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@ApplicationScoped
public class S3ClientProducer {

    @Produces
    @ApplicationScoped
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY")))
                .endpointOverride(URI.create("http://localhost:9444/s3"))
                .build();
    }
}
