package com.sergioruy.config;

import io.quarkus.logging.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public abstract class S3ClientResource {

    @ConfigProperty(name = "bucket.name")
    protected String bucketName;

    protected PutObjectRequest buildPutObjectRequest(final String uuid, final String mediaType) {
        Log.infof("Using bucket name: %s", bucketName);
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uuid)
                .contentType(mediaType)
                .build();
    }

    protected GetObjectRequest buildGetObjectRequest(final String uuid) {
        return GetObjectRequest.builder()
                .bucket(bucketName)
                .key(uuid)
                .build();
    }
}
