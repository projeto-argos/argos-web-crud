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
    <title>Página inicial - Argos</title>

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
    <jsp:param name="ativo" value="home"/>
</jsp:include>

<main class="conteudo">

    <h1 class="home-titulo">Total de registros</h1>

    <section class="cards-grade" aria-label="Total de registros por tabela">

        <a class="card-total" href="${ctx}/users">
            <div class="card-total-topo">
                <span class="card-total-rotulo">Total de usuários</span>
                <span class="card-total-icone"><i class="fa-solid fa-user"></i></span>
            </div>
            <p class="card-total-numero"><c:out value="${summary.totalUsers}" default="0"/></p>
        </a>

        <a class="card-total" href="${ctx}/properties">
            <div class="card-total-topo">
                <span class="card-total-rotulo">Total de propriedades</span>
                <span class="card-total-icone"><i class="fa-solid fa-location-dot"></i></span>
            </div>
            <p class="card-total-numero"><c:out value="${summary.totalProperties}" default="0"/></p>
        </a>

        <a class="card-total" href="${ctx}/animals">
            <div class="card-total-topo">
                <span class="card-total-rotulo">Total de animais</span>
                <span class="card-total-icone"><i class="fa-solid fa-cow"></i></span>
            </div>
            <p class="card-total-numero"><c:out value="${summary.totalAnimals}" default="0"/></p>
        </a>

        <a class="card-total" href="${ctx}/lots">
            <div class="card-total-topo">
                <span class="card-total-rotulo">Total de lotes</span>
                <span class="card-total-icone"><i class="fa-solid fa-handshake"></i></span>
            </div>
            <p class="card-total-numero"><c:out value="${summary.totalLots}" default="0"/></p>
        </a>

        <a class="card-total" href="${ctx}/herds">
            <div class="card-total-topo">
                <span class="card-total-rotulo">Total de rebanhos</span>
                <span class="card-total-icone"><i class="fa-solid fa-people-group"></i></span>
            </div>
            <p class="card-total-numero"><c:out value="${summary.totalHerds}" default="0"/></p>
        </a>

        <a class="card-total" href="${ctx}/admins">
            <div class="card-total-topo">
                <span class="card-total-rotulo">Total de admins</span>
                <span class="card-total-icone"><i class="fa-solid fa-user-gear"></i></span>
            </div>
            <p class="card-total-numero"><c:out value="${summary.totalAdmins}" default="0"/></p>
        </a>

    </section>

    <h2 class="home-titulo home-titulo-secao">Últimas atualizações</h2>

    <div class="home-tabela">
        <div class="tabela-scroll">
            <table class="tabela">
                <thead>
                <tr>
                    <th>Registro</th>
                    <th>Tipo</th>
                    <th>Atualizado em</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="registro" items="${summary.recentRecords}">
                    <tr class="linha">
                        <td><c:out value="${registro.name}"/></td>
                        <td><c:out value="${registro.type}"/></td>
                        <td><c:out value="${registro.formattedDate}"/></td>
                    </tr>
                </c:forEach>

                <c:if test="${empty summary.recentRecords}">
                    <tr class="linha-vazia">
                        <td colspan="3">Nenhum registro encontrado.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>

</main>

</body>
</html>
