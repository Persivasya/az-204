package com.example.azfunction.chat;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.CosmosDBOutput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import java.util.Optional;
import java.util.UUID;

public class CreateMessageFunction {

    @FunctionName("CreateMessageFunction")
    public HttpResponseMessage run(
        @HttpTrigger(
            name = "req",
            methods = { HttpMethod.POST },
            authLevel = AuthorizationLevel.ANONYMOUS
        ) HttpRequestMessage<Optional<String>> request,
        @CosmosDBOutput(
            name = "items",
            databaseName = "chat",
            containerName = "messages",
            connection = "CosmosDBConnection"
        ) OutputBinding<Message> output,
        final ExecutionContext context
    ) {
        String body = request.getBody().orElse("");
        Message message = new Message(UUID.randomUUID().toString(), body);
        output.setValue(message);
        return request
            .createResponseBuilder(HttpStatus.OK)
            .body(message)
            .build();
    }
}
