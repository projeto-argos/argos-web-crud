<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Lista de Usuários - Argos</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 10px; border: 1px solid #ccc; text-align: left; }
        th { background-color: #f4f4f4; }
        .btn { padding: 6px 12px; text-decoration: none; border-radius: 4px; color: white; display: inline-block; cursor: pointer; border: none; }
        .btn-add { background-color: #28a745; margin-bottom: 15px; }
        .btn-edit { background-color: #007bff; }
        .btn-delete { background-color: #dc3545; }
        .actions { display: flex; gap: 5px; }
        .status-active { color: green; font-weight: bold; }
        .status-inactive { color: red; font-weight: bold; }
    </style>
</head>
<body>

<h2>Gerenciamento de Usuários</h2>

<a href="${pageContext.request.contextPath}/users/register" class="btn btn-add">+ Novo Usuário</a>

<c:if test="${empty users}">
    <p>Nenhum usuário cadastrado.</p>
</c:if>

<c:if test="${not empty users}">
    <table>
        <thead>
        <tr>
            <th>Nome Completo</th>
            <th>CPF</th>
            <th>E-mail</th>
            <th>Telefone</th>
            <th>Perfil (Role)</th>
            <th>Data Nasc.</th>
            <th>Status</th>
            <th>Ações</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${users}">
            <tr>
                <td><c:out value="${user.fullName}" /></td>
                <td><c:out value="${user.cpf}" /></td>
                <td><c:out value="${user.email}" /></td>
                <td><c:out value="${user.phone}" /></td>
                <td><c:out value="${user.role}" /></td>
                <td><c:out value="${user.birthDate}" /></td>
                <td>
                    <c:choose>
                        <c:when test="${user.active}">
                            <span class="status-active">Ativo</span>
                        </c:when>
                        <c:otherwise>
                            <span class="status-inactive">Inativo</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="actions">
                    <a href="${pageContext.request.contextPath}/users/edit?id=${user.idUser}" class="btn btn-edit">Editar</a>

                    <form action="${pageContext.request.contextPath}/users/delete" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="${user.idUser}" />
                        <button type="submit" class="btn btn-delete" onclick="return confirm('Tem certeza que deseja excluir o usuário ${user.fullName}?')">
                            Excluir
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

</body>
</html>