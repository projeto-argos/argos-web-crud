<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/_helpers.jspf" %>
<%
    String ctx = request.getContextPath();
    // Edição: bean vindo do GET /edit; ou idParam reenviado no POST que falhou
    Map<String, Object> p = props(request.getAttribute("herd"));
    String editId = p.isEmpty() ? request.getParameter("idHerd") : idOf(p, "idHerd");
    boolean edit = editId != null && !editId.isBlank();
%>
<%= head("Rebanhos - " + (edit ? "Editar" : "Novo"), ctx) %>
<%= erroBox(request) %>
<form class="card" method="post" action="<%= ctx %>/herds/<%= edit ? "edit" : "register" %>">
<% if (edit) { %>
    <input type="hidden" name="idHerd" value="<%= esc(editId) %>">
    <p><small>ID: <code><%= esc(editId) %></code></small></p>
<% } %>
    <div class="grid">
    <%= fInput(request, p, "Nome", "name", "text", true, "") %>
    <%= fInput(request, p, "Raça", "breed", "text", false, "") %>
    <%= fInput(request, p, "Finalidade", "purpose", "text", false, "") %>
    <%= fInput(request, p, "Quantidade de cabeças", "headCount", "number", false, "min=\"0\" step=\"1\"") %>
    <%= fInput(request, p, "ID da propriedade (UUID)", "propertyId", "text", false, "placeholder=\"UUID\"") %>
    <%= fCheck(request, p, "Ativo", "active", edit) %>
    </div>
    <p style="margin-top:18px">
        <button type="submit"><%= edit ? "Salvar alterações" : "Cadastrar" %></button>
        <a class="btn sec" href="<%= ctx %>/herds">Cancelar</a>
    </p>
</form>
<%= foot() %>
