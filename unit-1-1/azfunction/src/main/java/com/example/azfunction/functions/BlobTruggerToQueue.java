package com.example.azfunction.functions;

import java.time.LocalDateTime;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.BlobTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.QueueOutput;

public class BlobTruggerToQueue {
    @FunctionName("BlobTruggerToQueue")
    public void run(
            @BlobTrigger(name = "file", path = "mycontainer/{name}", connection = "AzureWebJobsStorage") byte[] content,
            @QueueOutput(name = "output", queueName = "output", connection = "AzureWebJobsStorage") OutputBinding<byte[]> output,
            final ExecutionContext context) {
        output.setValue(content);
        context.getLogger().info("Java Blob trigger function executed at: " + LocalDateTime.now());
    }
}
