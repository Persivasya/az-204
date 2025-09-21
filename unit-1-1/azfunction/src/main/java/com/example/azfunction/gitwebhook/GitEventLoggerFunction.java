package com.example.azfunction.gitwebhook;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
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
        String signature256 = request.getHeaders().getOrDefault("X-Hub-Signature-256", "");
        String secret = System.getenv("GIT_SECRET_KEY");
        if (secret.isEmpty() || signature.isBlank()
                || !verifyGithubSignature(request.getBody().orElse(""), signature256, signature, secret, true,
                        context)) {
            return request.createResponseBuilder(HttpStatus.UNAUTHORIZED).body("Unauthorized").build();
        }

        String body = request.getBody().orElse("");
        RepoEvent event = new RepoEvent(UUID.randomUUID().toString(), body);
        output.setValue(event);
        context.getLogger().info("Java Git event logger function executed at: " + LocalDateTime.now());
        return request.createResponseBuilder(HttpStatus.OK).body(event).build();
    }

    static boolean verifyGithubSignature(String body, String sig256, String sig1, String secret,
            boolean allowSha1Fallback, ExecutionContext context) {
        if (secret == null || secret.isEmpty())
            return false;

        // Prefer SHA-256
        if (sig256 != null && sig256.startsWith("sha256=")) {
            String expected = "sha256=" + hmacHex("HmacSHA256", secret, body);
            context.getLogger().info("Expected: " + expected);
            context.getLogger().info("Sig256: " + sig256);
            return constantTimeEquals(expected, sig256);
        }

        // Legacy SHA-1 only if you explicitly allow it
        if (allowSha1Fallback && sig1 != null && sig1.startsWith("sha1=")) {
            String expected = "sha1=" + hmacHex("HmacSHA1", secret, body);
            context.getLogger().info("Expected: " + expected);
            context.getLogger().info("Sig1: " + sig1);
            return constantTimeEquals(expected, sig1);
        }
        return false;
    }

    static String hmacHex(String alg, String key, String data) {
        try {
            Mac mac = Mac.getInstance(alg);
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), alg));
            byte[] out = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(out.length * 2);
            for (byte b : out)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
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

}
