<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/_helpers.jspf" %>
<%
    String ctx = request.getContextPath();
    Collection<?> items = (Collection<?>) request.getAttribute("animals");
%>
<%= head("Animais", ctx) %>
<%= erroBox(request) %>
<p><a class="btn" href="<%= ctx %>/animals/register">+ Novo</a></p>
<%= table(items, "idAnimal", ctx, "/animals") %>
<p><small>Servlet: GET <code>/animals</code> &middot; atributo: <code>animals</code> &middot; total: <%= items == null ? 0 : items.size() %></small></p>
<%= foot() %>
