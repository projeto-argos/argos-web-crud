package br.com.argos.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Lê configurações e segredos (como TURNSTILE_SECRET).
 * Ordem: 1) variável de ambiente do sistema; 2) arquivo .env em src/main/resources.
 * Assim a chave nunca fica escrita no código nem vai para o Git.
 */
public final class AppConfig {

    // Lido uma única vez, quando a classe é carregada
    private static final Properties ENV = loadEnvFile();

    private AppConfig() {
    }

    /** Devolve o valor da configuração ou null se não existir (ou estiver em branco). */
    public static String get(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = ENV.getProperty(key);
        }
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    private static Properties loadEnvFile() {
        Properties props = new Properties();
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream(".env")) {
            if (input != null) {
                props.load(input);
            }
        } catch (IOException e) {
            // sem .env: continua só com as variáveis de ambiente
        }
        return props;
    }
}
