package br.com.argos.connection;

import br.com.argos.util.AppConfig;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.SessionCookieConfig;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ConnectionFactory.init();
        // Inicializado automaticamente

        SessionCookieConfig cookie = sce.getServletContext().getSessionCookieConfig();
        cookie.setAttribute("SameSite", "Lax");
        cookie.setSecure("true".equalsIgnoreCase(AppConfig.get("COOKIE_SECURE")));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Garante o fechamento limpo do pool HikariCP
        ConnectionFactory.closePool();
    }
}