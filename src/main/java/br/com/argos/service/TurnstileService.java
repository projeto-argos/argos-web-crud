package br.com.argos.service;

import br.com.argos.exceptions.ValidationException;
import br.com.argos.util.AppConfig;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Confere com a Cloudflare se o token do Turnstile ("você é humano") é válido. */
public class TurnstileService {

    private static final Logger LOG = Logger.getLogger(TurnstileService.class.getName());

    private static final String VERIFY_URL =
            "https://challenges.cloudflare.com/turnstile/v0/siteverify";

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(TIMEOUT)
            .build();

    public void verifyTurnstile(String token, String remoteIp) {
        if (token == null || token.isBlank()) {
            throw new ValidationException("Confirme que você é humano.");
        }

        // variável de ambiente ou arquivo .env (src/main/resources)
        String secret = AppConfig.get("TURNSTILE_SECRET");
        if (secret == null) {
            LOG.severe("TURNSTILE_SECRET não configurada (variável de ambiente ou arquivo .env).");
            throw new ValidationException("Verificação de segurança indisponível no momento. Tente novamente mais tarde.");
        }

        String params = "secret=" + URLEncoder.encode(secret, StandardCharsets.UTF_8)
                + "&response=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
        if (remoteIp != null && !remoteIp.isBlank()) {
            params += "&remoteip=" + URLEncoder.encode(remoteIp, StandardCharsets.UTF_8);
        }

        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(VERIFY_URL))
                    .timeout(TIMEOUT)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(params))
                    .build();

            HttpResponse<String> resposta =
                    client.send(req, HttpResponse.BodyHandlers.ofString());

            if (!resposta.body().replace(" ", "").contains("\"success\":true")) {
                throw new ValidationException("Não foi possível confirmar que você é humano.");
            }

        } catch (IOException e) {
            // inclui o timeout
            LOG.log(Level.WARNING, "Falha ao falar com a Cloudflare (Turnstile)", e);
            throw new ValidationException("Não foi possível verificar agora, tente novamente.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ValidationException("Não foi possível verificar agora, tente novamente.");
        }
    }
}
