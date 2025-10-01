package com.chekotovsky.azstorage;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.stereotype.Service;

@Service
public class StorageService {

    private final BlobServiceClient blobServiceClient;

    public StorageService() {
        String connectioString =
            "DefaultEndpointsProtocol=https;EndpointSuffix=core.windows.net;AccountName=someranname;AccountKey=JslIGqf11msIGD5/9yakCzqX89dKERMurI2gwMazreUedbpyvTuiFE1vxt3QPMlqAzdV1naR/PvM+ASttTRvng==;BlobEndpoint=https://someranname.blob.core.windows.net/;FileEndpoint=https://someranname.file.core.windows.net/;QueueEndpoint=https://someranname.queue.core.windows.net/;TableEndpoint=https://someranname.table.core.windows.net/";
        this.blobServiceClient = new BlobServiceClientBuilder()
            .connectionString(connectioString)
            .buildClient();
    }

    public void createContainer(String containerName) {
        blobServiceClient.createBlobContainerIfNotExists(containerName);
    }

    public void uploadFile(
        String containerName,
        String fileName,
        InputStream inputStream
    ) throws IOException {
        blobServiceClient
            .getBlobContainerClient(containerName)
            .getBlobClient(fileName)
            .upload(inputStream, inputStream.available());
    }

    public String getFileUrl(String containerName, String fileName) {
        return blobServiceClient
            .getBlobContainerClient(containerName)
            .getBlobClient(fileName)
            .getBlobUrl();
    }
}
