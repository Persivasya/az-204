package com.example.azfunction.gitwebhook;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.CosmosDBOutput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class GitEventLoggerFunction {

    @FunctionName("GitEventLoggerFunction")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST }, authLevel = AuthorizationLevel.FUNCTION, route = "gitwebhook") HttpRequestMessage<Optional<String>> request,
            @CosmosDBOutput(name = "items", databaseName = "git", containerName = "events", connection = "CosmosDBConnection") OutputBinding<RepoEvent> output,
            final ExecutionContext context) {
        String body = request.getBody().orElse("");
        RepoEvent event = new RepoEvent(UUID.randomUUID().toString(), body);
        output.setValue(event);
        context.getLogger().info("Java Git event logger function executed at: " + LocalDateTime.now());
        return request.createResponseBuilder(HttpStatus.OK).body(event).build();
    }
}
