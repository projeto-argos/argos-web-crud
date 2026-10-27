package br.com.argos.controller.adminServlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * {@code AuthFilter} é um filtro de autenticação que restringe o acesso
 * a rotas da aplicação apenas para administradores logados.
 * tem sessão com o atributo {@code "usuarioLogado"}, gravado pelo LoginServlet.
 * Quem não tem é redirecionado para {@code /login}.
 * <p>
 */
@WebFilter(urlPatterns = {"/home", "/admins/*", "/users/*", "/properties/*", "/animals/*", "/lots/*", "/herds/*", "/lookup"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        boolean loggedIn = (session != null && session.getAttribute("usuarioLogado") != null);

        if (loggedIn) {
            // impede o navegador de guardar a tela em cache
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
