package com.sam.twitchclone.service;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.specialized.BlockBlobClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AzureService {
    @Value("${azure.blob.storage.connection.string}")
    private String azureBlobStorageConnectionString;
    @Value("${image.bucket.name}")
    private String logoBucketName;

    private BlobContainerClient blobClientBuilder(String containerName) {
        return new BlobContainerClientBuilder()
                .connectionString(azureBlobStorageConnectionString)
                .containerName(containerName)
                .buildClient();
    }

    public String uploadImage(MultipartFile image) {
        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        String fileName = StringUtils.stripFilenameExtension(image.getOriginalFilename());
        long currentTime= System.currentTimeMillis();
        String key = "images/" + fileName + "_" +  currentTime + "." + extension;
//        return key;

        try {
            return uploadFile(image.getBytes(), logoBucketName, key, image.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("error while uploading image" );
        }
    }

    public String uploadFile(byte[] fileData, String containerName, String key, String contentType) {

        BlockBlobClient blockBlobClient = blobClientBuilder(containerName).getBlobClient(key).getBlockBlobClient();
        try (ByteArrayInputStream dataStream = new ByteArrayInputStream(fileData)) {
            blockBlobClient.upload(dataStream, fileData.length);
            blockBlobClient.setHttpHeaders(new BlobHttpHeaders().setContentType(contentType));
            return blockBlobClient.getBlobUrl();
        } catch (IOException e) {
            log.error("Error while uploading data to azure blob storage and error is ", e);
            throw new IllegalStateException("Can not upload file");
        }
    }

}
