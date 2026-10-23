package br.com.argos.controller.authServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {

    // PROCESSA O LOGOUT
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        // VERIFICA SE EXISTE UMA SESSÃO ATIVA
        HttpSession session = request.getSession(false);

        // ENCERRA A SESSÃO, SE EXISTIR
        if (session != null) {
            session.invalidate();
        }

        // REDIRECIONA PARA A PÁGINA DE LOGIN
        response.sendRedirect(
                request.getContextPath() + "/login"
        );
    }

    // PERMITE REALIZAR O LOGOUT POR UM FORMULÁRIO POST
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        // REUTILIZA O PROCESSAMENTO DO LOGOUT
        doGet(request, response);
    }
}