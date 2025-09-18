package com.example.azfunction.functions;

import java.time.LocalDateTime;
import java.util.Optional;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class HttpToQueue {
    @FunctionName("HttpToQueue")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = { HttpMethod.GET,
                    HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS, route = "hello") HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java Timer trigger function executed at: " + LocalDateTime.now());
        return request.createResponseBuilder(HttpStatus.OK).body("Hello, World!").build();
    }
}
