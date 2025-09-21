package com.example.azfunction.gitwebhook;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.OutputBinding;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.CosmosDBOutput;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class GitEventLoggerFunction {

    @FunctionName("GitEventLoggerFunction")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.POST }, authLevel = AuthorizationLevel.FUNCTION, route = "gitwebhook") HttpRequestMessage<Optional<String>> request,
            @CosmosDBOutput(name = "items", databaseName = "git", containerName = "events", connection = "CosmosDBConnection") OutputBinding<RepoEvent> output,
            final ExecutionContext context) {
        String signature = request.getHeaders().getOrDefault("X-Hub-Signature", "");
        String secret = System.getenv("GIT_SECRET_KEY");
        if (secret.isEmpty() || signature.isBlank()
                || !verifySha256(request.getBody().orElse("").getBytes(), secret, signature)) {
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED).body("Unauthorized").build();
        }

        String body = request.getBody().orElse("");
        RepoEvent event = new RepoEvent(UUID.randomUUID().toString(), body);
        output.setValue(event);
        context.getLogger().info("Java Git event logger function executed at: " + LocalDateTime.now());
        return request.createResponseBuilder(HttpStatus.OK).body(event).build();
    }

    private static boolean verifySha256(byte[] body, String secret, String sigHeader) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(Base64.getDecoder().decode(secret), "HmacSHA256"));
            byte[] hash = mac.doFinal(body);
            String expected = "sha256=" + Base64.getEncoder().encodeToString(hash);
            return expected.equals(sigHeader);
        } catch (Exception e) {
            return false;
        }
    }
}
