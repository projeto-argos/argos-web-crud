package br.com.argos.connection;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Inicializado automaticamente
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Garante o fechamento limpo do pool HikariCP
        ConnectionFactory.closePool();
    }
}