<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Tela de carregamento --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/carregamento.css">

<div class="carregamento" id="carregamento" role="status" aria-live="polite" aria-hidden="true">
    <div class="carregamento-cena" aria-hidden="true">
        <i class="carregamento-poeira"></i>
        <i class="carregamento-poeira"></i>
        <i class="carregamento-poeira"></i>
        <img class="carregamento-mascote"
             src="${pageContext.request.contextPath}/assets/img/mascote-correndo.webp"
             alt="" width="292" height="360">
        <span class="carregamento-sombra"></span>
        <span class="carregamento-chao"></span>
    </div>

    <p class="carregamento-texto">
        Carregando
        <span class="carregamento-pontos" aria-hidden="true"><span></span><span></span><span></span></span>
    </p>
</div>

<script src="${pageContext.request.contextPath}/assets/js/carregamento.js" defer></script>
