package com.example.azfunction.eventhubs;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.Cardinality;
import com.microsoft.azure.functions.annotation.EventHubOutput;
import com.microsoft.azure.functions.annotation.EventHubTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;
import java.util.List;

public class EventHubFunction {

    @FunctionName("EventHubFunction")
    public void run(
        @EventHubTrigger(
            name = "eventHubMessages",
            eventHubName = "testhub",
            connection = "EventHubConnection",
            cardinality = Cardinality.MANY,
            consumerGroup = "funccons"
        ) List<String> messages,
        @EventHubOutput(
            name = "eventHubMessages",
            eventHubName = "testoutputhub",
            connection = "EventHubConnection"
        ) OutputBinding<String> output,
        final ExecutionContext context
    ) {
        context.getLogger().info("Batch size is" + messages);
        messages.stream().forEach(output::setValue);
    }
}
