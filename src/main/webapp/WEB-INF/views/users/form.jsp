<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>${empty user ? "Novo Usuário" : "Editar Usuário"} - Argos</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="text"], input[type="email"], input[type="password"], input[type="date"] {
            width: 100%; padding: 8px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px;
        }
        .btn { padding: 8px 15px; text-decoration: none; border-radius: 4px; color: white; border: none; cursor: pointer; }
        .btn-save { background-color: #28a745; }
        .btn-back { background-color: #6c757d; margin-left: 10px; }
        .error { color: red; margin-bottom: 15px; }
    </style>
</head>
<body>

<h2>${empty user ? "Cadastrar Novo Usuário" : "Editar Usuário"}</h2>

<!-- Exibe mensagem de erro se houver -->
<c:if test="${not empty erro}">
    <div class="error"><c:out value="${erro}" /></div>
</c:if>

<!-- Define a URL de ação de forma segura sem usar o operador + -->
<c:choose>
    <c:when test="${empty user}">
        <c:url var="formAction" value="/users/register" />
    </c:when>
    <c:otherwise>
        <c:url var="formAction" value="/users/edit" />
    </c:otherwise>
</c:choose>

<form action="${formAction}" method="post">

    <!-- Campo oculto do ID necessário para a edição -->
    <input type="hidden" name="idUser" value="${user.idUser}" />

    <div class="form-group">
        <label for="fullName">Nome Completo:</label>
        <input type="text" id="fullName" name="fullName" value="${user.fullName}" required maxlength="120" />
    </div>

    <div class="form-group">
        <label for="cpf">CPF:</label>
        <input type="text" id="cpf" name="cpf" value="${user.cpf}" required maxlength="14" />
    </div>

    <div class="form-group">
        <label for="email">E-mail:</label>
        <input type="email" id="email" name="email" value="${user.email}" required maxlength="120" />
    </div>

    <div class="form-group">
        <label for="phone">Telefone:</label>
        <input type="text" id="phone" name="phone" value="${user.phone}" />
    </div>

    <div class="form-group">
        <label for="role">Perfil (Role):</label>
        <select id="role" name="role" required>
            <option value="" disabled ${empty user.role ? "selected" : ""}>Selecione um perfil...</option>
            <option value="ADMIN" ${user.role == 'ADMIN' ? "selected" : ""}>Administrador</option>
            <option value="USER" ${user.role == 'USER' ? "selected" : ""}>Usuário</option>
            <!-- Adicione mais opções aqui se precisar (ex: MANAGER, etc.) -->
        </select>
    </div>

    <div class="form-group">
        <label for="password">Senha: ${not empty user ? "(Deixe em branco para manter a atual)" : ""}</label>
        <input type="password" id="password" name="password" ${empty user ? "required" : ""} />
    </div>

    <div class="form-group">
        <label for="birthDate">Data de Nascimento:</label>
        <input type="date" id="birthDate" name="birthDate" value="${user.birthDate}" />
    </div>

    <div class="form-group">
        <label>
            <input type="checkbox" name="active" value="true" ${empty user || user.active ? "checked" : ""} /> Ativo
        </label>
    </div>

    <button type="submit" class="btn btn-save">Salvar</button>
    <a href="${pageContext.request.contextPath}/users" class="btn btn-back">Cancelar</a>

</form>

</body>
</html>