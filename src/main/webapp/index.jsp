<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/_helpers.jspf" %>
<%= head("Argos - páginas de teste", request.getContextPath()) %>
<div class="card"><ul>
    <li><a href="<%= request.getContextPath() %>/admins">Administradores</a> &mdash; <a href="<%= request.getContextPath() %>/admins/register">novo</a></li>
    <li><a href="<%= request.getContextPath() %>/users">Usuários</a> &mdash; <a href="<%= request.getContextPath() %>/users/register">novo</a></li>
    <li><a href="<%= request.getContextPath() %>/suppliers">Fornecedores</a> &mdash; <a href="<%= request.getContextPath() %>/suppliers/register">novo</a></li>
    <li><a href="<%= request.getContextPath() %>/properties">Propriedades</a> &mdash; <a href="<%= request.getContextPath() %>/properties/register">novo</a></li>
    <li><a href="<%= request.getContextPath() %>/herds">Rebanhos</a> &mdash; <a href="<%= request.getContextPath() %>/herds/register">novo</a></li>
    <li><a href="<%= request.getContextPath() %>/animals">Animais</a> &mdash; <a href="<%= request.getContextPath() %>/animals/register">novo</a></li>
</ul></div>
<%= foot() %>
