package br.com.argos.servlet;

import dao.FornecedorDAO;
import model.Fornecedor;
import br.com.argos.util.Validador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;


/**
 * Servlet responsável por:
 *  - GET  /fornecedor  -> lista todos os fornecedores (HTML gerado na mão)
 *  - POST /fornecedor  -> cadastra um novo fornecedor a partir do form HTML
 *
 * O objetivo aqui é mostrar o fluxo
 * Servlet -> DAO -> Servlet, sem se preocupar ainda com design da tela.
 */

@WebServlet("/fornecedor")
public class FornecedorServlet extends HttpServlet {

    // Em projeto real isso poderia vir de injeção/factory, mas por ora
    // instanciamos direto — mantém consistente com o padrão atual do grupo.
    private final FornecedorDAO fornecedorDAO = new FornecedorDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html; charset=UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            List<Fornecedor> fornecedores = fornecedorDAO.listarTodos();
            // ^ ajuste o nome do método se no seu FornecedorDAO ele se
            //   chamar diferente (ex: listar(), buscarTodos(), findAll()...)

            out.println("<html><head><meta charset='UTF-8'>");
            out.println("<title>Fornecedores - ARGOS</title></head><body>");
            out.println("<h1>Fornecedores cadastrados</h1>");

            out.println("<table border='1' cellpadding='6'>");
            out.println("<tr><th>Nome</th><th>CNPJ</th><th>Telefone</th><th>Ativo</th></tr>");

            for (Fornecedor f : fornecedores) {
                out.println("<tr>");
                out.println("<td>" + f.getNome() + "</td>");
                out.println("<td>" + f.getCnpj() + "</td>");
                out.println("<td>" + f.getTelefone() + "</td>");
                out.println("<td>" + (f.isAtivo() ? "Sim" : "Não") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("<hr>");
            out.println("<h2>Cadastrar novo fornecedor</h2>");

            // Form aponta pro mesmo Servlet, via POST
            out.println("<form method='POST' action='fornecedor'>");
            out.println("Nome: <input type='text' name='nome' required><br><br>");
            out.println("CNPJ: <input type='text' name='cnpj' required><br><br>");
            out.println("Telefone: <input type='text' name='telefone' required><br><br>");
            out.println("<button type='submit'>Cadastrar</button>");
            out.println("</form>");

            out.println("</body></html>");

        } catch (SQLException e) {
            // Aqui é o Servlet decidindo o que fazer com o erro que o DAO propagou
            System.err.println("Erro ao listar fornecedores: " + e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Erro ao carregar fornecedores.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String nome = req.getParameter("nome");
        String cnpj = req.getParameter("cnpj");
        String telefone = req.getParameter("telefone");

        // Validação ANTES de chamar o DAO — usando o Validador que já existe no projeto
        if (!Validador.telefoneValido(telefone)) {
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().println(
                    "<p>Telefone inválido. <a href='fornecedor'>Voltar</a></p>");
            return;
        }
        // Se existir Validador.cnpjValido(...), chame aqui também.
        // (o resumo do projeto só menciona cpfValido/telefoneValido/emailValido
        //  até agora — confirmar com o grupo se falta um pro CNPJ)

        Fornecedor novo = new Fornecedor();
        novo.setNome(nome);
        novo.setCnpj(cnpj);
        novo.setTelefone(telefone);
        novo.setAtivo(true);

        try {
            fornecedorDAO.inserir(novo);
            // Depois de cadastrar, redireciona de volta pro GET (evita reenvio do form no F5)
            resp.sendRedirect("fornecedor");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar fornecedor: " + e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Erro ao cadastrar fornecedor.");
        }
    }
}