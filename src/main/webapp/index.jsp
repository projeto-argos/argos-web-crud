<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Página inicial (/): manda para o login. A landing fica no repositório argos-landingpage. --%>
<% response.sendRedirect(request.getContextPath() + "/login"); %>
