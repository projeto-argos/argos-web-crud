
package br.com.argos.controller.authServlet;

import br.com.argos.exceptions.ValidationException;
import br.com.argos.model.Admin;
import br.com.argos.service.AdminService;
import br.com.argos.service.TurnstileService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    // ATRIBUTOS
    private AdminService adminService;
    private TurnstileService turnstileService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        adminService = new AdminService();
        turnstileService = new TurnstileService();
    }

    // EXIBE PAGINA DE LOGIN
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        request.getRequestDispatcher(
                "/WEB-INF/views/auth/login.jsp"
        ).forward(request, response);
    }

    // PROCESSA LOGIN
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // SESSAO PARA CONTAR AS TENTATIVAS
        HttpSession session = request.getSession(true);

        try {
            // RECEBE DADOS
            String email =
                    request.getParameter("email");

            String password =
                    request.getParameter("password");

            // VERIFICA SE EMAIL E SENHA FORAM PASSADOS
            if (email == null ||
                    email.isBlank() ||
                    password == null ||
                    password.isBlank()) {
                request.setAttribute("erro", "Informe o e-mail e a senha.");
                request.getRequestDispatcher(
                        "/WEB-INF/views/auth/login.jsp"
                        ).forward(request, response);
                return;
            }

            // VERIFICA SE NAO E ROBO
            String turnstileToken =
                    request.getParameter("cf-turnstile-response");
            try {
                turnstileService.verifyTurnstile(turnstileToken, request.getRemoteAddr());
            } catch (ValidationException e) {
                request.setAttribute("erro", e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
                return;
            }

            // VERIFICA SE ESTA BLOQUEADO
            LocalDateTime lockedUntil =
                    (LocalDateTime) session.getAttribute("lockedUntil");

            if (lockedUntil != null &&
                    lockedUntil.isAfter(LocalDateTime.now())) {
                request.setAttribute("erro", "Muitas tentativas. Tente novamente em 5 minutos.");
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
                return;
            }

            // CRIA SESSAO
            Admin admin = adminService.authenticate(email, password);

            // LOGIN CERTO, ZERA AS TENTATIVAS
            session.removeAttribute("attempts");

            // TROCA O ID DA SESSAO
            request.changeSessionId();

            // ADMIN LOGADO
            Map<String, String> loggedUser = new HashMap<>();
            loggedUser.put("id", admin.getIdAdmin().toString());
            loggedUser.put("name", admin.getFullName());
            loggedUser.put("type", "ADMIN");
            session.setAttribute("loggedUser", loggedUser);
            session.setAttribute("loggedAdminId", admin.getIdAdmin());

            // REDIRECIONA
            response.sendRedirect(
                    request.getContextPath() + "/welcome");

        } catch (ValidationException e) {
            // CONTA A TENTATIVA ERRADA
            Integer attempts =
                    (Integer) session.getAttribute("attempts");

            if (attempts == null) {
                attempts = 0;
            }
            attempts = attempts + 1;
            session.setAttribute("attempts", attempts);

            // BLOQUEIA DEPOIS DE 5 ERROS
            if (attempts >= 5) {
                session.setAttribute("lockedUntil", LocalDateTime.now().plusMinutes(5));
                session.setAttribute("attempts", 0);
            }

            // TRATA ERRO
            request.setAttribute("erro", "E-mail ou senha inválidos.");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }
}
