package com.example.azfunction.durablefunctions;

import java.time.LocalDateTime;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.durabletask.azurefunctions.DurableActivityTrigger;

public class AprovalFunction {

    @FunctionName("AprovalFunction")
    public String run(
            @DurableActivityTrigger(name = "approval") Report report,
            final ExecutionContext context) throws InterruptedException {
        if (LocalDateTime.now().toLocalTime().getSecond() % 3 == 0) {
            report.setStatus(ReportStatus.COMPLETED);
        } else if (LocalDateTime.now().toLocalTime().getSecond() % 3 == 1) {
            System.out.println("Sleeping for 10 seconds");
            Thread.sleep(10000);
        } else {
            report.setStatus(ReportStatus.FAILED);
        }
        return report.getStatus().toString();
    }
}
