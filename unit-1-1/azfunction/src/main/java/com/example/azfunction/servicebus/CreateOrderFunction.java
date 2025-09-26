package com.example.azfunction.servicebus;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.annotation.ServiceBusQueueOutput;
import java.util.Optional;

public class CreateOrderFunction {

    @FunctionName("CreateOrderFunction")
    public void run(
        @HttpTrigger(
            name = "req",
            methods = { HttpMethod.POST },
            authLevel = AuthorizationLevel.ANONYMOUS
        ) HttpRequestMessage<Optional<String>> request,
        @ServiceBusQueueOutput(
            name = "out",
            queueName = "cosm-queue",
            connection = "ServiceBusConnection"
        ) OutputBinding<String> output,
        final ExecutionContext context
    ) {
        String body = request.getBody().orElse("");
        output.setValue(body);
    }
}
