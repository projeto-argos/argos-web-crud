<%--
  Created by IntelliJ IDEA.
  User: Murilo Paiva
  Date: 17/09/2026
  Time: 12:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Boas-vindas - Argos</title>

    <!-- Fontes do Figma  -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;700&family=Rubik:wght@500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <!-- CSS -->
    <link rel="stylesheet" href="${ctx}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${ctx}/assets/css/tables.css">
    <link rel="stylesheet" href="${ctx}/assets/css/home.css">

    <!-- JAVASCRIPT -->
    <script src="${ctx}/assets/js/sidebar.js"></script>
</head>
<body>

<jsp:include page="/WEB-INF/views/components/_sidebar.jsp">
    <jsp:param name="ativo" value="welcome"/>
</jsp:include>

<main class="conteudo conteudo-cheio">
    <section class="boas-vindas">

        <svg class="onda onda-topo" viewBox="0 0 420 240" aria-hidden="true">
            <path style="fill:var(--onda-1)" d="M0 0H420V200C340 215 270 170 215 115C170 70 90 30 0 0Z"/>
            <path style="fill:var(--onda-2)" d="M60 0H420V170C350 185 290 140 245 95C210 60 140 20 60 0Z"/>
            <path style="fill:var(--onda-3)" d="M130 0H420V130C360 150 300 110 265 75C235 45 190 15 130 0Z"/>
        </svg>
        <svg class="onda onda-base" viewBox="0 0 420 240" aria-hidden="true">
            <path style="fill:var(--onda-1)" d="M0 0H420V200C340 215 270 170 215 115C170 70 90 30 0 0Z"/>
            <path style="fill:var(--onda-2)" d="M60 0H420V170C350 185 290 140 245 95C210 60 140 20 60 0Z"/>
            <path style="fill:var(--onda-3)" d="M130 0H420V130C360 150 300 110 265 75C235 45 190 15 130 0Z"/>
        </svg>

        <h1>Bem-vindo à gestão de dados administrativos do ARGOS!</h1>
        <p>Registre, altere, delete e veja todos os dados.<br>Como podemos ajudar você hoje?</p>

        <a class="btn-entrar" href="${ctx}/home">
            Entrar <i class="fa-solid fa-arrow-right"></i>
        </a>

        <img class="mascote" src="${ctx}/assets/img/mascote-boas-vindas.png" alt="Mascote do ARGOS acenando">

    </section>
</main>

</body>
</html>
