<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/_helpers.jspf" %>
<%
    String ctx = request.getContextPath();
    Collection<?> items = (Collection<?>) request.getAttribute("properties");
%>
<%= head("Propriedades", ctx) %>
<%= erroBox(request) %>
<p><a class="btn" href="<%= ctx %>/properties/register">+ Novo</a></p>
<%= table(items, "idProperty", ctx, "/properties") %>
<p><small>Servlet: GET <code>/properties</code> &middot; atributo: <code>properties</code> &middot; total: <%= items == null ? 0 : items.size() %></small></p>
<%= foot() %>
