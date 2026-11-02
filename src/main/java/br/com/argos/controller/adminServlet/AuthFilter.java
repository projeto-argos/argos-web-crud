package br.com.argos.controller.adminServlet;

import br.com.argos.service.AdminService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * {@code AuthFilter} só deixa passar quem
 * tem sessão com o atributo {@code "loggedUser"} do tipo {@code "ADMIN"}, gravado pelo LoginServlet.
 * Quem não tem é redirecionado para {@code /login}.
 * <p>
 * Também confere, de tempos em tempos, se o admin da sessão ainda existe e está ativo no banco
 * se ele foi excluído por outro admin, a sessão é encerrada na hora.
 */
@WebFilter(urlPatterns = {"/home", "/welcome", "/admins/*", "/users/*", "/properties/*", "/animals/*", "/lots/*", "/herds/*"})
public class AuthFilter implements Filter {

    // De quanto em quanto tempo o admin da sessão é conferido no banco
    private static final long CHECK_INTERVAL_MS = 60_000;

    private AdminService adminService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        adminService = new AdminService();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);

        HttpSession session = req.getSession(false);

        // só "ADMIN" entra no painel
        boolean loggedIn = session != null
                && session.getAttribute("loggedUser") instanceof Map<?, ?> user
                && "ADMIN".equals(user.get("type"));

        // admin apagado depois do login, derruba a sessão
        if (loggedIn && !adminStillActive(session)) {
            session.invalidate();
            loggedIn = false;
        }

        if (loggedIn) {
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    /** Confere no banco se o admin da sessão ainda existe, no máximo 1 vez por minuto por sessão. */
    private boolean adminStillActive(HttpSession session) {
        Object id = session.getAttribute("loggedAdminId");
        if (!(id instanceof UUID adminId)) {
            return false;
        }

        long now = System.currentTimeMillis();
        Object lastCheck = session.getAttribute("adminCheckedAt");
        if (lastCheck instanceof Long ms && now - ms < CHECK_INTERVAL_MS) {
            return true;
        }

        // findById só devolve admin ativo
        if (adminService.findById(adminId) == null) {
            return false;
        }
        session.setAttribute("adminCheckedAt", now);
        return true;
    }

    @Override
    public void destroy() {}
}
