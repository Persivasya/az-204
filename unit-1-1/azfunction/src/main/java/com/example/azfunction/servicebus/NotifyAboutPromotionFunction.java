package com.example.azfunction.servicebus;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.ServiceBusTopicOutput;
import com.microsoft.azure.functions.annotation.TimerTrigger;

public class NotifyAboutPromotionFunction {

    @FunctionName("NotifyAboutPromotionFunction")
    public void run(
        @TimerTrigger(
            name = "timerInfo",
            schedule = "0 * * * * *"
        ) String timerInfo,
        @ServiceBusTopicOutput(
            name = "promotion",
            topicName = "cosm-topic",
            connection = "ServiceBusConnection",
            subscriptionName = "cosm-topic-sub-1"
        ) OutputBinding<String> output,
        final ExecutionContext context
    ) {
        output.setValue("Promotion is available!!!");
    }
}
