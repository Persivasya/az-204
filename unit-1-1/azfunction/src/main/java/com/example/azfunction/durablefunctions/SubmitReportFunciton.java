package com.example.azfunction.durablefunctions;

import com.example.azfunction.functions.Optional;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class SubmitReportFunciton {
    @FunctionName("SubmitReportFunciton")
    public HttpResponseMessage run(
        @HttpTrigger(
            name = "req",
            methods = {HttpMethod.POST},
            authLevel = AuthorizationLevel.ANONYMOUS,
            route = "submitreport")
            HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context
    ) {
        return request.createResponseBuilder(HttpStatus.OK).body("Hello, World!").build();
    }
}
