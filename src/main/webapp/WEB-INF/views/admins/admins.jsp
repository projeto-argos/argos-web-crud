<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/_helpers.jspf" %>
<%
    String ctx = request.getContextPath();
    Collection<?> items = (Collection<?>) request.getAttribute("admins");
%>
<%= head("Administradores", ctx) %>
<%= erroBox(request) %>
<p><a class="btn" href="<%= ctx %>/admins/register">+ Novo</a></p>
<%= table(items, "idAdmin", ctx, "/admins") %>
<p><small>Servlet: GET <code>/admins</code> &middot; atributo: <code>admins</code> &middot; total: <%= items == null ? 0 : items.size() %></small></p>
<%= foot() %>
