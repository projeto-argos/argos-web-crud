<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/_helpers.jspf" %>
<%
    String ctx = request.getContextPath();
    Collection<?> items = (Collection<?>) request.getAttribute("herds");
%>
<%= head("Rebanhos", ctx) %>
<%= erroBox(request) %>
<p><a class="btn" href="<%= ctx %>/herds/register">+ Novo</a></p>
<%= table(items, "idHerd", ctx, "/herds") %>
<p><small>Servlet: GET <code>/herds</code> &middot; atributo: <code>herds</code> &middot; total: <%= items == null ? 0 : items.size() %></small></p>
<%= foot() %>
