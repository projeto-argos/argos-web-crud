<%--
  Created by IntelliJ IDEA.
  User: Murilo Paiva
  Date: 17/09/2026
  Time: 12:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- Botão do menu no celular --%>
<button type="button" class="menu-mobile" aria-label="Abrir menu" aria-expanded="false">
    <i class="fa-solid fa-bars"></i>
</button>
<div class="sidebar-fundo"></div>

<nav class="sidebar">
    <header>
        <img class="logo-completo"
             src="${pageContext.request.contextPath}/assets/img/logo-argos.png"
             alt="Argos">

        <img class="logo-icone"
             src="${pageContext.request.contextPath}/assets/img/logo-argos-icone.png"
             alt="Argos">

        <button type="button" class="toggle"
                aria-label="Abrir ou fechar menu">
            <i class="fa-solid fa-chevron-right"></i>
        </button>

        <button type="button" class="menu-fechar" aria-label="Fechar menu">
            <i class="fa-solid fa-xmark"></i>
        </button>
    </header>

    <div class="menu-bar">
        <ul class="menu-links">
            <li><a href="${pageContext.request.contextPath}/home" class="${param.ativo == 'home' ? 'active' : ''}"><i class="fa-solid fa-house icon"></i><span class="text">Página inicial</span></a></li>
            <li><a href="${pageContext.request.contextPath}/users" class="${param.ativo == 'user' ? 'active' : ''}"><i class="fa-solid fa-user icon"></i><span class="text">Usuário</span></a></li>
            <li><a href="${pageContext.request.contextPath}/properties" class="${param.ativo == 'propriedade' ? 'active' : ''}"><i class="fa-solid fa-location-dot icon"></i><span class="text">Propriedade</span></a></li>
            <li><a href="${pageContext.request.contextPath}/animals" class="${param.ativo == 'animal' ? 'active' : ''}"><i class="fa-solid fa-cow icon"></i><span class="text">Animal</span></a></li>
            <li><a href="${pageContext.request.contextPath}/lots" class="${param.ativo == 'lot' ? 'active' : ''}"><i class="fa-solid fa-handshake icon"></i><span class="text">Lote</span></a></li>
            <li><a href="${pageContext.request.contextPath}/herds" class="${param.ativo == 'rebanho' ? 'active' : ''}"><i class="fa-solid fa-people-group icon"></i><span class="text">Rebanho</span></a></li>
            <li><a href="${pageContext.request.contextPath}/admins" class="${param.ativo == 'admin' ? 'active' : ''}"><i class="fa-solid fa-user-gear icon"></i><span class="text">Admin</span></a></li>
        </ul>

        <ul class="bottom-content">
            <li>
                <button type="button" class="mode-btn">
                    <span class="icon moon"><i class="fa-solid fa-moon"></i></span>
                    <span class="icon sun"><i class="fa-solid fa-sun"></i></span>
                    <span class="text mode-text">Modo escuro</span>
                    <span class="switch"></span>
                </button>
            </li>
            <li><a href="${pageContext.request.contextPath}/logout"><i class="fa-solid fa-right-from-bracket icon"></i><span class="text">Sair</span></a></li>
        </ul>
    </div>
</nav>

<jsp:include page="/WEB-INF/views/components/_carregamento.jsp"/>
