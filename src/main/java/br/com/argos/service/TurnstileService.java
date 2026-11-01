package br.com.argos.service;

import br.com.argos.exceptions.ValidationException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class TurnstileService {

    private static final String VERIFY_URL =
            "https://challenges.cloudflare.com/turnstile/v0/siteverify";

    private final HttpClient client = HttpClient.newHttpClient();

    public void verifyTurnstile(String token, String remoteIp) {
        if (token == null || token.isBlank()) {
            throw new ValidationException("Confirme que você é humano.");
        }

        String secret = System.getenv("TURNSTILE_SECRET");
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("Variável TURNSTILE_SECRET não configurada.");
        }

        String params = "secret=" + URLEncoder.encode(secret, StandardCharsets.UTF_8)
                + "&response=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
        if (remoteIp != null && !remoteIp.isBlank()) {
            params += "&remoteip=" + URLEncoder.encode(remoteIp, StandardCharsets.UTF_8);
        }

        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(VERIFY_URL))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(params))
                    .build();

            HttpResponse<String> resposta =
                    client.send(req, HttpResponse.BodyHandlers.ofString());

            if (!resposta.body().replace(" ", "").contains("\"success\":true")) {
                throw new ValidationException("Não foi possível confirmar que você é humano.");
            }

        } catch (IOException e) {
            throw new ValidationException("Não foi possível verificar agora, tente novamente.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ValidationException("Não foi possível verificar agora, tente novamente.");
        }
    }
}