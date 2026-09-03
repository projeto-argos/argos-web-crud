package br.com.argos.servlet;

import br.com.argos.dao.FornecedorDAO;
import br.com.argos.model.Fornecedor;
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

@WebServlet("/fornecedor")
public class FornecedorServlet extends HttpServlet {
    private final FornecedorDAO fornecedorDAO = new FornecedorDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/html; charset=UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            List<Fornecedor> fornecedores = fornecedorDAO.listarTodos();


            out.write("<html><head><meta charset='UTF-8'>");
            out.write("<title>Fornecedores - ARGOS</title></head><body>");
            out.println("<h1>Fornecedores cadastrados</h1>");

            out.println("<table border='1' cellpadding='6'>");
            // Adicionado a coluna E-mail na tabela
            out.println("<tr><th>Nome</th><th>CNPJ</th><th>Telefone</th><th>E-mail</th><th>Ativo</th></tr>");

            for (Fornecedor f : fornecedores) {
                out.println("<tr>");
                out.println("<td>" + f.getNome() + "</td>");
                out.println("<td>" + f.getCnpj() + "</td>");
                out.println("<td>" + f.getTelefone() + "</td>");
                out.println("<td>" + f.getEmail() + "</td>"); // Exibindo o email
                out.println("<td>" + (f.isAtivo() ? "Sim" : "Não") + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("<hr>");
            out.println("<h2>Cadastrar novo fornecedor</h2>");

            // Form aponta pro mesmo Servlet, via POST
            out.println("<form method='POST' action='" + req.getContextPath() + "/fornecedor'>");            out.println("Nome: <input type='text' name='nome' required><br><br>");
            out.println("CNPJ: <input type='text' name='cnpj' required><br><br>");
            out.println("Telefone: <input type='text' name='telefone' required><br><br>");
            out.println("E-mail: <input type='email' name='email' required><br><br>"); // Input de email adicionado
            out.println("<button type='submit'>Cadastrar</button>");
            out.println("</form>");

            out.println("</body></html>");

        } catch (SQLException e) {
            System.err.println("Erro ao listar fornecedores: " + e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Erro ao carregar fornecedores.");
        }
    }


//  TESTE SE O TOMCAT NÃO FUNCIONAR

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//
//        System.out.println("=================================");
//        System.out.println("1 - ENTROU NO DOGET");
//        System.out.println("=================================");
//
//        try {
//            // 1. Busca os dados no banco PRIMEIRO
//            System.out.println("2 - CHAMANDO DAO.LISTARTODOS()");
//            List<Fornecedor> fornecedores = fornecedorDAO.listarTodos();
//            System.out.println("3 - QUANTIDADE DE FORNECEDORES: " + (fornecedores != null ? fornecedores.size() : "null"));
//
//            // 2. Prepara a resposta HTML
//            resp.setContentType("text/html; charset=UTF-8");
//
//            // 3. Escreve o HTML somente após obter os dados com sucesso
//            try (PrintWriter out = resp.getWriter()) {
//                out.println("<!DOCTYPE html>");
//                out.println("<html>");
//                out.println("<head>");
//                out.println("<meta charset='UTF-8'>");
//                out.println("<title>Fornecedores - ARGOS</title>");
//                out.println("</head>");
//                out.println("<body>");
//
//                out.println("<h1>Fornecedores cadastrados</h1>");
//                out.println("<table border='1' cellpadding='6'>");
//                out.println("<tr>");
//                out.println("<th>Nome</th>");
//                out.println("<th>CNPJ</th>");
//                out.println("<th>Telefone</th>");
//                out.println("<th>E-mail</th>");
//                out.println("<th>Ativo</th>");
//                out.println("</tr>");
//
//                if (fornecedores != null) {
//                    for (Fornecedor f : fornecedores) {
//                        out.println("<tr>");
//                        out.println("<td>" + f.getNome() + "</td>");
//                        out.println("<td>" + f.getCnpj() + "</td>");
//                        out.println("<td>" + f.getTelefone() + "</td>");
//                        out.println("<td>" + f.getEmail() + "</td>");
//                        out.println("<td>" + (f.isAtivo() ? "Sim" : "Não") + "</td>");
//                        out.println("</tr>");
//                    }
//                }
//
//                out.println("</table>");
//                out.println("<hr>");
//
//                out.println("<h2>Cadastrar novo fornecedor</h2>");
//                out.println("<form method='POST' action='" + req.getContextPath() + "/fornecedor'>");
//                out.println("Nome: <input type='text' name='nome' required><br><br>");
//                out.println("CNPJ: <input type='text' name='cnpj' required><br><br>");
//                out.println("Telefone: <input type='text' name='telefone' required><br><br>");
//                out.println("E-mail: <input type='email' name='email' required><br><br>");
//                out.println("<button type='submit'>Cadastrar</button>");
//                out.println("</form>");
//
//                out.println("</body>");
//                out.println("</html>");
//
//                System.out.println("4 - HTML GERADO COM SUCESSO");
//            }
//
//        } catch (Exception e) {
//            // Captura QUALQUER erro (NullPointerException, ClassNotFoundException, SQLException, etc.)
//            System.err.println("=================================");
//            System.err.println("ERRO DETECTADO NO DOGET:");
//            e.printStackTrace();
//            System.err.println("=================================");
//
//            if (!resp.isCommitted()) {
//                resp.sendError(
//                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
//                        "Erro ao processar requisição: " + e.getMessage()
//                );
//            }
//        }
//    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String nome = req.getParameter("nome");
        String cnpj = req.getParameter("cnpj");
        String telefone = req.getParameter("telefone");
        String email = req.getParameter("email"); // Capturando o email do formulário

        // Validação ANTES de chamar o DAO
        if (!Validador.telefoneValido(telefone)) {
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().println(
                    "<p>Telefone inválido. <a href='fornecedor'>Voltar</a></p>");
            return;
        }


        if (!Validador.cnpjValido(cnpj)) {
            resp.setContentType("text/html; charset=UTF-8");
            resp.getWriter().println(
                    "<p>CNPJ inválido. <a href='fornecedor'>Voltar</a></p>");
            return;
        }


        Fornecedor novo = new Fornecedor();
        novo.setNome(nome);
        novo.setCnpj(cnpj);
        novo.setTelefone(telefone);
        novo.setEmail(email);
        novo.setAtivo(true);

        try {
            fornecedorDAO.inserir(novo);
            // Depois de cadastrar, redireciona de volta pro GET (evita reenvio do form no F5)
            resp.sendRedirect(req.getContextPath() + "/fornecedor");
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar fornecedor: " + e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Erro ao cadastrar fornecedor.");
        }
    }
}