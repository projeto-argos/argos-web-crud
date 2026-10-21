<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/_helpers.jspf" %>
<%
    String ctx = request.getContextPath();
    Collection<?> items = (Collection<?>) request.getAttribute("users");
%>
<%= head("Usuários", ctx) %>
<%= erroBox(request) %>
<p><a class="btn" href="<%= ctx %>/users/register">+ Novo</a></p>
<%= table(items, "idUser", ctx, "/users") %>
<p><small>Servlet: GET <code>/users</code> &middot; atributo: <code>users</code> &middot; total: <%= items == null ? 0 : items.size() %></small></p>
<%= foot() %>
