package br.com.argos.filter;

import br.com.argos.util.AppConfig;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Coloca cabeçalhos de segurança em todas as respostas
 * Eles instruem o navegador a bloquear ataques comuns
 */
@WebFilter("/*")
public class SecurityHeadersFilter implements Filter {

    /**
     * Content-Security-Policy (CSP): lista do que a página pode carregar.
     * Tudo que não está na lista é bloqueado pelo navegador.
     */
    private static final String CSP = String.join("; ",
            "default-src 'self'",
            "script-src 'self' https://challenges.cloudflare.com",
            "style-src 'self' https://fonts.googleapis.com https://cdnjs.cloudflare.com",
            "style-src-attr 'unsafe-inline'",
            "font-src 'self' https://fonts.gstatic.com https://cdnjs.cloudflare.com",
            "img-src 'self' data:",
            "frame-src https://challenges.cloudflare.com",
            "connect-src 'self' https://challenges.cloudflare.com",
            "form-action 'self'",
            "base-uri 'self'",
            "object-src 'none'",
            // ninguém pode colocar o sistema dentro de um iframe
            "frame-ancestors 'none'"
    );

    private final boolean reportOnly = "true".equalsIgnoreCase(AppConfig.get("CSP_REPORT_ONLY"));

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        resp.setHeader(reportOnly ? "Content-Security-Policy-Report-Only" : "Content-Security-Policy", CSP);

        resp.setHeader("X-Content-Type-Options", "nosniff");

        // não abrir dentro de iframe
        resp.setHeader("X-Frame-Options", "DENY");

        // não vaza o endereço completo
        resp.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

        // o sistema não usa câmera, microfone nem localização
        resp.setHeader("Permissions-Policy", "camera=(), microphone=(), geolocation=()");

        // HSTS:
        boolean https = req.isSecure() || "https".equalsIgnoreCase(req.getHeader("X-Forwarded-Proto"));
        if (https) {
            resp.setHeader("Strict-Transport-Security", "max-age=15552000");
        }

        chain.doFilter(request, response);
    }
}
