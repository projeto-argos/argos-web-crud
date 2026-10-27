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
<c:set var="urlInserir" value="${ctx}/admins/register"/>
<c:set var="urlAtualizar" value="${ctx}/admins/edit"/>
<c:set var="urlExcluir" value="${ctx}/admins/delete"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admins - Argos</title>

    <!-- Fontes do Figma  -->
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
        <jsp:param name="ativo" value="admin"/>
    </jsp:include>

    <main class="conteudo">
        <c:set var="textoMensagem" value="${not empty mensagem ? mensagem : erro}"/>
        <c:set var="tipoAviso" value="${not empty erro or tipoMensagem == 'erro' or tipoMensagem == 'error' ? 'erro' : tipoMensagem}"/>
        <c:if test="${not empty textoMensagem}">
            <div class="toast ${tipoAviso == 'erro' ? 'toast-erro' : 'toast-sucesso'}" role="status">
                <span class="toast-icone"><i class="fa-solid ${tipoAviso == 'erro' ? 'fa-exclamation' : 'fa-check'}"></i></span>
                <div class="toast-texto"><strong><c:out value="${textoMensagem}"/></strong>
                    <c:if test="${not empty detalheMensagem}"><p><c:out value="${detalheMensagem}"/></p></c:if>
                </div>
                <button type="button" class="toast-fechar" aria-label="Fechar aviso"><i class="fa-solid fa-xmark"></i></button>
            </div>
        </c:if>
                </div>
                <button type="button" class="toast-fechar" aria-label="Fechar aviso"><i class="fa-solid fa-xmark"></i></button>
            </div>
        </c:if>

        <section class="painel">

            <div class="painel-topo">
                <h1>Admins cadastrados</h1>

                <c:set var="filtrando" value="${not empty param.fullName or not empty param.cpf or not empty param.email or not empty param.phone}"/>
                <details class="filtro ${filtrando ? 'filtro-ativo' : ''}">
                    <summary>Filtrar</summary>
                    <form method="get" action="${ctx}/PATH">
                            <div class="campo"><label for="filtro-fullName">Nome completo</label><input type="text" id="filtro-fullName" name="fullName" value="<c:out value='${param.fullName}'/>"/></div>
                            <div class="campo"><label for="filtro-cpf">CPF</label><input type="text" id="filtro-cpf" name="cpf" value="<c:out value='${param.cpf}'/>"/></div>
                            <div class="campo"><label for="filtro-email">E-mail</label><input type="text" id="filtro-email" name="email" value="<c:out value='${param.email}'/>"/></div>
                            <div class="campo"><label for="filtro-phone">Telefone</label><input type="text" id="filtro-phone" name="phone" value="<c:out value='${param.phone}'/>"/></div>
                        <div class="filtro-acoes"><a href="${ctx}/admins" class="btn-pilula btn-neutro">Limpar</a><button type="submit" class="btn-pilula">Aplicar filtro</button></div>
                    </form>
                </details>
                <button type="button" class="btn btn-escuro"
                        data-abrir="modalAdmin" data-acao="criar" data-action="${urlInserir}"
                        data-titulo="Cadastrar Admin" data-botao="Cadastrar">
                    <i class="fa-solid fa-plus"></i> Novo admin
                </button>
            </div>

            <div class="busca">
                <i class="fa-solid fa-magnifying-glass"></i>
                <input type="search" id="campoBusca" placeholder="Buscar admin ..." aria-label="Buscar admin" autocomplete="off">
            </div>

            <div class="tabela-scroll">
                <table class="tabela" id="tabela">
                    <thead>
                        <tr>
                            <th>Nome completo</th>
                            <th>CPF</th>
                            <th>Email</th>
                            <th>Telefone</th>
                            <th class="col-acoes">Ações</th>
                        </tr>
                    </thead>
                    <tbody>

                        <c:forEach var="admin" items="${admins}">
                            <tr class="linha">
                                <td><c:out value="${admin.fullName}"/></td>
                                <td class="cpf"><c:out value="${admin.cpf}"/></td>
                                <td><c:out value="${admin.email}"/></td>
                                <td class="telefone"><c:out value="${admin.phone}"/></td>
                                <td class="col-acoes">
                                    <button type="button" class="btn-icone btn-editar" title="Editar" aria-label="Editar admin"
                                            data-abrir="modalAdmin" data-acao="editar" data-action="${urlAtualizar}"
                                            data-titulo="Editar Admin" data-botao="Salvar"
                                            data-id-admin="<c:out value='${admin.idAdmin}'/>"
                                            data-full-name="<c:out value='${admin.fullName}'/>"
                                            data-cpf="<c:out value='${admin.cpf}'/>"
                                            data-email="<c:out value='${admin.email}'/>"
                                            data-phone="<c:out value='${admin.phone}'/>">
                                        <i class="fa-solid fa-pen"></i>
                                    </button>
                                    <button type="button" class="btn-icone btn-excluir" title="Excluir" aria-label="Excluir admin"
                                            data-abrir="modalExcluir"
                                            data-id="<c:out value='${admin.idAdmin}'/>"
                                            data-full-name="<c:out value='${admin.fullName}'/>">
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
        <dialog class="modal" id="modalAdmin">
            <div class="modal-caixa">
                <div class="modal-topo">
                    <h2>Cadastrar Admin</h2>
                    <button type="button" class="btn-fechar" data-fechar aria-label="Fechar"><i class="fa-solid fa-xmark"></i></button>
                </div>

                <form method="post" action="${urlInserir}">
                    <input type="hidden" name="idAdmin" value="">

                    <div class="form-grade">
                        <div class="campo">
                            <label for="campoNome">Nome Completo</label>
                            <input type="text" id="campoNome" name="fullName" placeholder="ex: Amanda Rodrigues Lima"
                                   required maxlength="120" autocomplete="off">
                        </div>
                        <div class="campo">
                            <label for="campoCpf">CPF</label>
                            <input type="text" id="campoCpf" name="cpf" placeholder="ex: 12345678901"
                                   required inputmode="numeric" maxlength="11" pattern="\d{11}"
                                   title="Digite os 11 números do CPF, sem pontos e traço" autocomplete="off">
                        </div>
                        <div class="campo">
                            <label for="campoEmail">Email</label>
                            <input type="email" id="campoEmail" name="email" placeholder="ex: adriana@gmail.com"
                                   required maxlength="120" autocomplete="off">
                        </div>
                        <div class="campo">
                            <label for="campoTelefone">Telefone</label>
                            <input type="tel" id="campoTelefone" name="phone" placeholder="ex: 11999999999"
                                   required inputmode="numeric" maxlength="11" pattern="\d{10,11}"
                                   title="DDD + número, só números (10 ou 11 dígitos)" autocomplete="off">
                        </div>
                        <div class="campo largura-total" data-so-cadastro>
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
                    <h2>Excluir Admin</h2>
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
