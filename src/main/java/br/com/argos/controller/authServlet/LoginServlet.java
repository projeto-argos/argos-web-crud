package br.com.argos.controller.authServlet;

import br.com.argos.model.Admin;
import br.com.argos.service.AdminService;
import br.com.argos.exceptions.ValidationException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    // ATRIBUTO
    private AdminService adminService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        adminService = new AdminService();
    }

    // EXIBE A PÁGINA DE LOGIN
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/auth/login.jsp"
        ).forward(request, response);
    }

    // PROCESSA O LOGIN
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // RECEBE OS DADOS DO FORMULÁRIO
        String email =
                request.getParameter("email");

        String password =
                request.getParameter("password");

        // VERIFICA SE OS CAMPOS FORAM PREENCHIDOS
        if (email == null || email.isBlank()
                || password == null || password.isBlank()) {

            request.setAttribute(
                    "erro",
                    "Informe o e-mail e a senha."
            );

            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);

            return;
        }
        try {
            // AUTENTICA O ADMINISTRADOR
            Admin admin = adminService.authenticate(email, password);

            // VERIFICA SE JÁ EXISTE UMA SESSÃO
            HttpSession previousSession = request.getSession(false);

            // ENCERRA A SESSÃO ANTERIOR, SE EXISTIR
            if (previousSession != null) {
                previousSession.invalidate();
            }

            // CRIA UMA NOVA SESSÃO
            HttpSession session = request.getSession(true);

            // ARMAZENA OS DADOS DO ADMINISTRADOR
            Map<String, String> loggedAdmin = new HashMap<>();
            loggedAdmin.put("id", admin.getIdAdmin().toString());
            loggedAdmin.put("name", admin.getFullName());
            loggedAdmin.put("type", "ADMIN");

            session.setAttribute("usuarioLogado", loggedAdmin);

            // REDIRECIONA
            response.sendRedirect(
                    request.getContextPath() + "/admins");

        } catch (ValidationException e) {
            // EXIBE ERRO DE AUTENTICAÇÃO
            request.setAttribute("erro", "E-mail ou senha inválidos.");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);

        } catch (Exception e) {
            // TRATA ERROS INESPERADOS
            throw new ServletException("Erro ao processar o login.", e);
        }
    }
}