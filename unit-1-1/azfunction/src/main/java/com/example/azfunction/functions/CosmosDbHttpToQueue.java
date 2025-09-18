package com.example.azfunction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.CosmosDBInput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class CosmosDbHttpToQueue {
    @FunctionName("CosmosDbHttpToQueue")
    public HttpResponseMessage run(
        @HttpTrigger(
            name = "req",
            methods = {HttpMethod.GET},
            authLevel = AuthorizationLevel.ANONYMOUS,
            route = "document"
        )
        HttpRequestMessage<Optional<String>> request,
        @CosmosDBInput(
            name = "items",
            databaseName = "function-1",
            containerName = "test",
            connection = "CosmosDBConnection",
            partitionKey = "{id}") List<Document> input,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java Cosmos DB trigger function executed at: " + LocalDateTime.now());
        if (input.isEmpty()) {
            return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                          .body("Document not found.")
                          .build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK)
                          .header("Content-Type", "application/json")
                          .body(input)
                          .build();
        }
    }
}
