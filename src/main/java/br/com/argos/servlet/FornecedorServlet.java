package br.com.argos.servlet;


import br.com.argos.dao.SupplierDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet("/fornecedor")
public class FornecedorServlet extends HttpServlet {
    private final SupplierDAO supplierDAO = new SupplierDAO();

}