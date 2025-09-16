package com.example.azfunction;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.CosmosDBOutput;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.OutputBinding;

import java.time.LocalDateTime;
import java.util.Optional;

public class AddToCosmosDbToQueue {
    @FunctionName("AddToCosmosDbToQueue")
    public HttpResponseMessage run(
        @HttpTrigger(
            name = "req",
            methods = {HttpMethod.POST},
            authLevel = AuthorizationLevel.ANONYMOUS,
            route = "document") HttpRequestMessage<Optional<String>> request,
        @CosmosDBOutput(
            name = "document",
            databaseName = "function-1",
            containerName = "test",
            connection = "CosmosDBConnection") OutputBinding<Document> output,
        final ExecutionContext context
    ) {
        String body = request.getBody().orElse("");
        Document document = new Document("generic", body);
        output.setValue(document);
        context.getLogger().info("Java Cosmos DB trigger function executed at: " + LocalDateTime.now());
        return request.createResponseBuilder(HttpStatus.OK).body(document).build();
    }
}
