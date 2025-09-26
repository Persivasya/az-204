package com.example.azfunction.servicebus;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.QueueTrigger;

public class GetArticleFromQueueFunction {

    @FunctionName("GetArticleFromQueueFunction")
    public void run(
        @QueueTrigger(
            name = "order",
            queueName = "queue-test",
            connection = "QueueBusConnection"
        ) String message,
        final ExecutionContext context
    ) {
        context.getLogger().info("Article was recieved: " + message);
    }
}
