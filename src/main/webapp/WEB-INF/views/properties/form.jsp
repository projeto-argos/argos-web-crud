<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/_helpers.jspf" %>
<%
    String ctx = request.getContextPath();
    // Edição: bean vindo do GET /edit; ou idParam reenviado no POST que falhou
    Map<String, Object> p = props(request.getAttribute("property"));
    String editId = p.isEmpty() ? request.getParameter("idProperty") : idOf(p, "idProperty");
    boolean edit = editId != null && !editId.isBlank();
%>
<%= head("Propriedades - " + (edit ? "Editar" : "Novo"), ctx) %>
<%= erroBox(request) %>
<form class="card" method="post" action="<%= ctx %>/properties/<%= edit ? "edit" : "register" %>">
<% if (edit) { %>
    <input type="hidden" name="idProperty" value="<%= esc(editId) %>">
    <p><small>ID: <code><%= esc(editId) %></code></small></p>
<% } %>
    <div class="grid">
    <%= fInput(request, p, "Nome", "name", "text", true, "") %>
    <%= fInput(request, p, "CNPJ", "cnpj", "text", false, "maxlength=\"18\"") %>
    <%= fInput(request, p, "Telefone", "phone", "tel", false, "") %>
    <%= fInput(request, p, "ID do usuário (UUID)", "userId", "text", false, "placeholder=\"UUID\"") %>
    <%= fInput(request, p, "ID do endereço (UUID)", "addressId", "text", false, "placeholder=\"UUID\"") %>
    <%= fCheck(request, p, "Ativo", "active", edit) %>
    </div>
    <p style="margin-top:18px">
        <button type="submit"><%= edit ? "Salvar alterações" : "Cadastrar" %></button>
        <a class="btn sec" href="<%= ctx %>/properties">Cancelar</a>
    </p>
</form>
<%= foot() %>
