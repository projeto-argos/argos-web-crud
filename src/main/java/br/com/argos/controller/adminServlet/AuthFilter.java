package br.com.argos.controller.adminServlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * {@code AuthFilter} porta das telas do painel: só deixa passar quem
 * tem sessão com o atributo {@code "usuarioLogado"} do tipo {@code "ADMIN"}, gravado pelo LoginServlet.
 * Quem não tem é redirecionado para {@code /login}.
 * <p>
 */
@WebFilter(urlPatterns = {"/home", "/welcome", "/admins/*", "/users/*", "/properties/*", "/animals/*", "/lots/*", "/herds/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

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
                && session.getAttribute("usuarioLogado") instanceof Map<?, ?> user
                && "ADMIN".equals(user.get("type"));

        if (loggedIn) {
            resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            resp.setDateHeader("Expires", 0);
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    @Override
    public void destroy() {}
}
