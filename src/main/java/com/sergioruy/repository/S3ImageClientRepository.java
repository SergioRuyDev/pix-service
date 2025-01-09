package com.sergioruy.repository;

import com.sergioruy.config.S3ClientResource;
import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.nio.file.Path;

@ApplicationScoped
public class S3ImageClientRepository extends S3ClientResource {

    public static final String IMAGE_PNG = "image/png";

    private final S3Client s3Client;

    public S3ImageClientRepository(S3Client s3Client) {
        this.s3Client = s3Client;
    }


    public PutObjectResponse putObject(Path file, String uuid) {
        var putObjectResponse = s3Client.putObject(buildPutObjectRequest(uuid, IMAGE_PNG), RequestBody.fromFile(file.toFile()));
        assert null != putObjectResponse;
        return putObjectResponse;
    }

    public ResponseBytes<GetObjectResponse> getObjects(final String uuid) {
        try {
            return s3Client.getObjectAsBytes(buildGetObjectRequest(uuid));
        } catch (NoSuchKeyException noSuchKeyException) {
            return null;
        }
    }

}
