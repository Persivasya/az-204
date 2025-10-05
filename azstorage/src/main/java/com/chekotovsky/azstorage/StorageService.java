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
        String connectioString = "AZURE_STORAGE_CONNECTION_STRING";
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
