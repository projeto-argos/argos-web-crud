<%--
  Created by IntelliJ IDEA.
  User: Murilo Paiva
  Date: 17/09/2026
  Time: 12:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="urlInserir" value="${ctx}/users/register"/>
<c:set var="urlAtualizar" value="${ctx}/users/edit"/>
<c:set var="urlExcluir" value="${ctx}/users/delete"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Usuários - Argos</title>

    <!-- Fontes usadas no Figma  -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;700&family=Rubik:wght@500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <!-- CSS -->
    <link rel="stylesheet" href="${ctx}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${ctx}/assets/css/tables.css">
    <link rel="stylesheet" href="${ctx}/assets/css/forms.css">
    <link rel="stylesheet" href="${ctx}/assets/css/feedbackMessages.css">

    <!-- JAVASCRIPT -->
    <script src="${ctx}/assets/js/sidebar.js"></script>
    <script src="${ctx}/assets/js/popup.js" defer></script>
    <script src="${ctx}/assets/js/tabela.js" defer></script>
</head>
<body>

<jsp:include page="/WEB-INF/views/components/_sidebar.jsp">
    <jsp:param name="ativo" value="user"/>
</jsp:include>

<main class="conteudo">

    <c:if test="${not empty mensagem}">
        <div class="toast ${tipoMensagem == 'erro' ? 'toast-erro' : 'toast-sucesso'}" role="status">
            <span class="toast-icone"><i class="fa-solid ${tipoMensagem == 'erro' ? 'fa-exclamation' : 'fa-check'}"></i></span>
            <div class="toast-texto">
                <strong><c:out value="${mensagem}"/></strong>
                <c:if test="${not empty detalheMensagem}">
                    <p><c:out value="${detalheMensagem}"/></p>
                </c:if>
            </div>
            <button type="button" class="toast-fechar" aria-label="Fechar aviso"><i class="fa-solid fa-xmark"></i></button>
        </div>
    </c:if>

    <section class="painel">

        <div class="painel-topo">
            <h1>Usuários cadastrados</h1>

            <form method="get" action="${ctx}/users" class="filtro">
                <label>Nome completo
                    <input type="text" name="name" value="<c:out value='${user.name}'/>" placeholder="Ex: João Silva">
                </label>
                <%-- repetir para id, cpf, email, telefone --%>
                <button type="submit">Aplicar Filtro</button>
                <a href="${ctx}/users" class="btn">Limpar</a>
            </form>

            <button type="button" class="btn btn-escuro"
                    data-abrir="modalUser" data-acao="criar" data-action="${urlInserir}"
                    data-titulo="Cadastrar Usuário" data-botao="Cadastrar">
                <i class="fa-solid fa-plus"></i> Novo usuário
            </button>
        </div>

        <div class="busca">
            <i class="fa-solid fa-magnifying-glass"></i>
            <input type="search" id="campoBusca" placeholder="Buscar usuário ..." aria-label="Buscar usuário" autocomplete="off">
        </div>

        <div class="tabela-scroll">
            <table class="tabela" id="tabela">
                <thead>
                <tr>
                    <th>Nome completo</th>
                    <th>CPF</th>
                    <th>Email</th>
                    <th>Cargo</th>
                    <th class="col-acoes">Ações</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="user" items="${users}">
                    <tr class="linha">
                        <td><c:out value="${user.fullName}"/></td>
                        <td class="cpf"><c:out value="${user.cpf}"/></td>
                        <td><c:out value="${user.email}"/></td>
                        <td><c:out value="${user.role}"/></td>
                        <td class="col-acoes">
                            <button type="button" class="btn-icone btn-editar" title="Editar" aria-label="Editar usuário"
                                    data-abrir="modalUser" data-acao="editar" data-action="${urlAtualizar}"
                                    data-titulo="Editar Usuário" data-botao="Salvar"
                                    data-id-user="<c:out value='${user.idUser}'/>"
                                    data-full-name="<c:out value='${user.fullName}'/>"
                                    data-cpf="<c:out value='${user.cpf}'/>"
                                    data-phone="<c:out value='${user.phone}'/>"
                                    data-email="<c:out value='${user.email}'/>"
                                    data-birth-date="<c:out value='${user.birthDate}'/>"
                                    data-role="<c:out value='${user.role}'/>">
                                <i class="fa-solid fa-pen"></i>
                            </button>
                            <button type="button" class="btn-icone btn-excluir" title="Excluir" aria-label="Excluir usuário"
                                    data-abrir="modalExcluir"
                                    data-id="<c:out value='${user.idUser}'/>"
                                    data-full-name="<c:out value='${user.fullName}'/>">
                                <i class="fa-solid fa-trash-can"></i>
                            </button>
                        </td>
                    </tr>
                </c:forEach>

                <tr class="linha-vazia" id="linhaVazia" hidden>
                    <td colspan="5">Nenhum resultado encontrado.</td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="painel-rodape">
            <p id="infoPaginas" aria-live="polite"></p>
            <div class="paginacao">
                <button type="button" class="btn btn-contorno" id="btnAnterior">Anterior</button>
                <button type="button" class="btn btn-escuro" id="btnProxima">Próxima</button>
            </div>
        </div>

    </section>

    <%-- POP-UP --%>
    <dialog class="modal" id="modalUser">
        <div class="modal-caixa">
            <div class="modal-topo">
                <h2>Cadastrar Usuário</h2>
                <button type="button" class="btn-fechar" data-fechar aria-label="Fechar"><i class="fa-solid fa-xmark"></i></button>
            </div>

            <form method="post" action="${urlInserir}">
                <input type="hidden" name="idUser" value="">

                <div class="form-grade">
                    <div class="campo">
                        <label for="campoCpf">CPF</label>
                        <input type="text" id="campoCpf" name="cpf" placeholder="ex: 12345678901"
                               required inputmode="numeric" maxlength="11" pattern="\d{11}"
                               title="Digite os 11 números do CPF, sem pontos e traço" autocomplete="off">
                    </div>
                    <div class="campo">
                        <label for="campoTelefone">Telefone</label>
                        <input type="tel" id="campoTelefone" name="phone" placeholder="ex: 11999999999"
                               required inputmode="numeric" maxlength="11" pattern="\d{10,11}"
                               title="DDD + número, só números (10 ou 11 dígitos)" autocomplete="off">
                    </div>
                    <div class="campo largura-total">
                        <label for="campoNome">Nome Completo</label>
                        <input type="text" id="campoNome" name="fullName" placeholder="ex: Adriana Francesca Lima"
                               required maxlength="120" autocomplete="off">
                    </div>
                    <div class="campo">
                        <label for="campoEmail">Email</label>
                        <input type="email" id="campoEmail" name="email" placeholder="ex: adriana@gmail.com"
                               required maxlength="120" autocomplete="off">
                    </div>
                    <div class="campo">
                        <label for="campoNascimento">Data de Nascimento</label>
                        <input type="date" id="campoNascimento" name="birthDate" required>
                    </div>
                    <div class="campo">
                        <label for="campoCargo">Cargo</label>
                        <input type="text" id="campoCargo" name="role" placeholder="ex: Médico Veterinário"
                               required maxlength="50" autocomplete="off">
                    </div>
                    <div class="campo" data-so-cadastro>
                        <label for="campoSenha">Senha</label>
                        <input type="password" id="campoSenha" name="password" placeholder="Defina a senha de acesso"
                               required autocomplete="new-password">
                    </div>
                </div>

                <div class="form-rodape">
                    <button type="submit" class="btn-pilula">Cadastrar</button>
                </div>
            </form>
        </div>
    </dialog>

    <%-- POP-UP --%>
    <dialog class="modal modal-pequeno" id="modalExcluir">
        <div class="modal-caixa">
            <div class="modal-topo">
                <h2>Excluir Usuário</h2>
                <button type="button" class="btn-fechar" data-fechar aria-label="Fechar"><i class="fa-solid fa-xmark"></i></button>
            </div>

            <form method="post" action="${urlExcluir}">
                <input type="hidden" name="id" value="">

                <p class="modal-texto">Tem certeza que deseja excluir <strong data-mostrar="fullName"></strong>?</p>

                <div class="form-rodape">
                    <button type="button" class="btn-pilula btn-neutro" data-fechar>Cancelar</button>
                    <button type="submit" class="btn-pilula btn-perigo">Excluir</button>
                </div>
            </form>
        </div>
    </dialog>

</main>

</body>
</html>
