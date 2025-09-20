package com.example.azfunction.durablefunctions;

import java.time.LocalDateTime;
import java.util.Optional;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.durabletask.DurableTaskClient;
import com.microsoft.durabletask.azurefunctions.DurableClientContext;
import com.microsoft.durabletask.azurefunctions.DurableClientInput;

public class SubmitReportFunciton {
        @FunctionName("SubmitReportFunciton")
        public HttpResponseMessage run(
                        @HttpTrigger(name = "req", methods = {
                                        HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS, route = "submitreport") HttpRequestMessage<Optional<String>> request,
                        @DurableClientInput(name = "durableContxt") DurableClientContext clientContext,
                        final ExecutionContext context) {
                Report report = new Report(1, "Report 1", "Report 1 description", ReportStatus.PENDING);

                DurableTaskClient client = clientContext.getClient();
                String instanceId = client.scheduleNewOrchestrationInstance("ReportOrchestratorFunction", report);
                return clientContext.createCheckStatusResponse(request, instanceId);
        }
}
