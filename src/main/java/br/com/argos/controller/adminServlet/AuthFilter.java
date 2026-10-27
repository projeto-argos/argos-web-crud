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
 * <p>
 * e verifica se há uma sessão ativa com o atributo {@code "adminLoggedIn"} presente.
 * Caso contrário, o usuário é redirecionado para a página inicial ({@code index.jsp}).
 * </p>
 * */
@WebFilter(urlPatterns = {"/admin/*", "/home", "/users/*", "/properties/*", "/animals/*", "/lots/*", "/herds/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        boolean adminLoggedIn = (session != null && session.getAttribute("adminLoggedIn") != null);

        if (adminLoggedIn) {
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }

    @Override
    public void destroy() {}
}
