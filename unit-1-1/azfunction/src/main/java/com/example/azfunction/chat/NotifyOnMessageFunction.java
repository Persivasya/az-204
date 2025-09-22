package com.example.azfunction.chat;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.CosmosDBTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.signalr.annotation.SignalROutput;
import java.util.List;

public class NotifyOnMessageFunction {

    @FunctionName("NotifyOnMessageFunction")
    public void run(
        @CosmosDBTrigger(
            name = "items",
            databaseName = "chat",
            containerName = "messages",
            connection = "CosmosDBConnection"
        ) List<Message> messages,
        @SignalROutput(
            name = "chat",
            hubName = "messages",
            connectionStringSetting = "AzureSignalRConnectionString"
        ) OutputBinding<Message> output,
        final ExecutionContext context
    ) {
        for (Message message : messages) {
            output.setValue(message);
        }
    }
}
