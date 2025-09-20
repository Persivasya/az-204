package com.example.azfunction.durablefunctions;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.durabletask.azurefunctions.DurableActivityTrigger;

public class SendToReworkFunction {
    @FunctionName("SendToReworkFunction")
    public String run(
            @DurableActivityTrigger(name = "sendToRework") Report report,
            final ExecutionContext context) {
        report.setStatus(ReportStatus.REWORK);
        return report.getStatus().toString();
    }
}
