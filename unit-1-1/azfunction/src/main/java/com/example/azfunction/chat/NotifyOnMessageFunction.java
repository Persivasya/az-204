package com.example.azfunction.chat;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.CosmosDBTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.signalr.SignalRMessage;
import com.microsoft.azure.functions.signalr.annotation.SignalROutput;
import java.util.ArrayList;
import java.util.List;

public class NotifyOnMessageFunction {

    @FunctionName("NotifyOnMessageFunction")
    public void run(
        @CosmosDBTrigger(
            name = "items",
            databaseName = "chat",
            containerName = "messages",
            leaseContainerName = "chatLeases",
            createLeaseContainerIfNotExists = true,
            connection = "CosmosDBConnection"
        ) List<Message> messages,
        @SignalROutput(
            name = "chat",
            hubName = "messages",
            connectionStringSetting = "AzureSignalRConnectionString"
        ) OutputBinding<SignalRMessage[]> output,
        final ExecutionContext context
    ) {
        List<SignalRMessage> messagesToSend = new ArrayList<>();
        for (Message message : messages) {
            SignalRMessage messageToSend = new SignalRMessage(
                "newMessage",
                message
            );
            messagesToSend.add(messageToSend);
        }
        output.setValue(messagesToSend.toArray(new SignalRMessage[0]));
    }
}
