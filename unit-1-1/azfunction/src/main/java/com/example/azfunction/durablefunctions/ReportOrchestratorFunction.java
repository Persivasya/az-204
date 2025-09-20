package com.example.azfunction.durablefunctions;

import java.time.Duration;

import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.durabletask.Task;
import com.microsoft.durabletask.TaskOrchestrationContext;
import com.microsoft.durabletask.azurefunctions.DurableOrchestrationTrigger;

public class ReportOrchestratorFunction {
    @FunctionName("ReportOrchestratorFunction")
    public String run(
            @DurableOrchestrationTrigger(name = "reportOrchestrator") TaskOrchestrationContext context) {
        Report report = context.getInput(Report.class);
        report.setStatus(ReportStatus.PROCESSING);

        Task<String> status = context.callActivity("AprovalFunction", report, String.class);
        Task<Void> deadline = context.createTimer(Duration.ofSeconds(5));

        Task<?> winner = context.anyOf(status, deadline).await();

        if (winner == status) {
            return status.await();
        } else {
            Task<String> rework = context.callActivity("SendToReworkFunction", report, String.class);
            return rework.await();
        }
    }
}
