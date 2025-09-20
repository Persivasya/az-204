package com.example.azfunction.durablefunctions;

import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.durabletask.TaskOrchestrationContext;
import com.microsoft.durabletask.azurefunctions.DurableOrchestrationTrigger;

public class ReportOrchestratorFunction {
    @FunctionName("ReportOrchestratorFunction")
    public String run(
            @DurableOrchestrationTrigger(name = "reportOrchestrator") TaskOrchestrationContext context) {
        Report report = context.getInput(Report.class);

        String status = context.callActivity("AprovalFunction", report, String.class).await();
        return status;
    }
}
