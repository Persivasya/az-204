package com.example.azfunction.chat;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.CosmosDBInput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import java.util.List;
import java.util.Optional;

public class GetMwssagesFunciton {

    @FunctionName("GetMessagesFunction")
    public HttpResponseMessage run(
        @HttpTrigger(
            name = "req",
            methods = { HttpMethod.GET },
            authLevel = AuthorizationLevel.ANONYMOUS
        ) HttpRequestMessage<Optional<String>> request,
        @CosmosDBInput(
            name = "items",
            databaseName = "chat",
            containerName = "messages",
            sqlQuery = "SELECT c.id, c.message FROM c",
            connection = "CosmosDBConnection"
        ) List<Message> messages,
        final ExecutionContext context
    ) {
        return request
            .createResponseBuilder(HttpStatus.OK)
            .body(messages)
            .build();
    }
}
