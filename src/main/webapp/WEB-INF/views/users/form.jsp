<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/_helpers.jspf" %>
<%
    String ctx = request.getContextPath();
    // Edição: bean vindo do GET /edit; ou idParam reenviado no POST que falhou
    Map<String, Object> p = props(request.getAttribute("user"));
    String editId = p.isEmpty() ? request.getParameter("idUser") : idOf(p, "idUser");
    boolean edit = editId != null && !editId.isBlank();
%>
<%= head("Usuários - " + (edit ? "Editar" : "Novo"), ctx) %>
<%= erroBox(request) %>
<form class="card" method="post" action="<%= ctx %>/users/<%= edit ? "edit" : "register" %>">
<% if (edit) { %>
    <input type="hidden" name="idUser" value="<%= esc(editId) %>">
    <p><small>ID: <code><%= esc(editId) %></code></small></p>
<% } %>
    <div class="grid">
    <%= fInput(request, p, "Nome completo", "fullName", "text", true, "") %>
    <%= fInput(request, p, "CPF", "cpf", "text", true, "maxlength=\"14\"") %>
    <%= fInput(request, p, "E-mail", "email", "email", true, "") %>
    <%= fInput(request, p, "Telefone", "phone", "tel", false, "") %>
    <%= fInput(request, p, "Perfil (role)", "role", "text", true, "placeholder=\"ex.: PRODUTOR\"") %>
    <%= fInput(request, p, edit ? "Senha (vazio = manter a atual)" : "Senha", "password", "password", !edit, "autocomplete=\"new-password\"") %>
    <%= fInput(request, p, "Data de nascimento", "birthDate", "date", false, "") %>
    <%= fCheck(request, p, "Ativo", "active", edit) %>
    </div>
    <p style="margin-top:18px">
        <button type="submit"><%= edit ? "Salvar alterações" : "Cadastrar" %></button>
        <a class="btn sec" href="<%= ctx %>/users">Cancelar</a>
    </p>
</form>
<%= foot() %>
