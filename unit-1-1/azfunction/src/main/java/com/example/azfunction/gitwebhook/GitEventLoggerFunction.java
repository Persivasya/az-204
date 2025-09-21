package com.example.azfunction.gitwebhook;

import java.nio.charset.StandardCharsets;
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
                || !verifyGitHubSha1(request.getBody().orElse(""), secret, signature)) {
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED).body("Unauthorized").build();
        }

        String body = request.getBody().orElse("");
        RepoEvent event = new RepoEvent(UUID.randomUUID().toString(), body);
        output.setValue(event);
        context.getLogger().info("Java Git event logger function executed at: " + LocalDateTime.now());
        return request.createResponseBuilder(HttpStatus.OK).body(event).build();
    }

    static boolean verifyGitHubSha1(String rawBody, String signatureHeader, String secret) {
        if (signatureHeader == null || !signatureHeader.startsWith("sha1="))
            return false;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA1"));
            byte[] digest = mac.doFinal(rawBody.getBytes(StandardCharsets.UTF_8));

            String expected = "sha1=" + toHex(digest); // hex lowercase
            return constantTimeEquals(expected, signatureHeader);
        } catch (Exception e) {
            return false;
        }
    }

    static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null)
            return false;
        int diff = a.length() ^ b.length();
        for (int i = 0; i < Math.min(a.length(), b.length()); i++)
            diff |= a.charAt(i) ^ b.charAt(i);
        return diff == 0;
    }

    static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte x : bytes)
            sb.append(String.format("%02x", x));
        return sb.toString();
    }

}
