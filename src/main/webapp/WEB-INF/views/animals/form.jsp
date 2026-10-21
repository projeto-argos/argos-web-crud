<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/_helpers.jspf" %>
<%
    String ctx = request.getContextPath();
    // Edição: bean vindo do GET /edit; ou idParam reenviado no POST que falhou
    Map<String, Object> p = props(request.getAttribute("animal"));
    String editId = p.isEmpty() ? request.getParameter("idAnimal") : idOf(p, "idAnimal");
    boolean edit = editId != null && !editId.isBlank();
%>
<%= head("Animais - " + (edit ? "Editar" : "Novo"), ctx) %>
<%= erroBox(request) %>
<form class="card" method="post" action="<%= ctx %>/animals/<%= edit ? "edit" : "register" %>">
<% if (edit) { %>
    <input type="hidden" name="idAnimal" value="<%= esc(editId) %>">
    <p><small>ID: <code><%= esc(editId) %></code></small></p>
<% } %>
    <div class="grid">
    <%= fInput(request, p, "Brinco (earTag)", "earTag", "text", true, "") %>
    <%= fInput(request, p, "Peso (kg)", "weight", "number", false, "min=\"0\" step=\"any\"") %>
    <%= fInput(request, p, "Data de nascimento", "birthDate", "date", false, "") %>
    <%= fInput(request, p, "ID do lote (UUID)", "batchId", "text", false, "placeholder=\"UUID\"") %>
    <%= fInput(request, p, "ID do lote de origem (UUID)", "originBatchId", "text", false, "placeholder=\"UUID\"") %>
    <%= fInput(request, p, "Motivo da exceção", "exceptionReason", "text", false, "") %>
    <%= fInput(request, p, "Início da exceção", "exceptionStartDate", "date", false, "") %>
    <%= fInput(request, p, "Fim da exceção", "exceptionEndDate", "date", false, "") %>
    <%= fArea(request, p, "Observações", "notes") %>
    <%= fCheck(request, p, "Liberado para abate", "clearedForSlaughter", edit) %>
    <%= fCheck(request, p, "Ativo", "active", edit) %>
    </div>
    <p style="margin-top:18px">
        <button type="submit"><%= edit ? "Salvar alterações" : "Cadastrar" %></button>
        <a class="btn sec" href="<%= ctx %>/animals">Cancelar</a>
    </p>
</form>
<%= foot() %>
