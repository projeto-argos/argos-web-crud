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
<c:set var="urlInserir" value="${ctx}/herds/register"/>
<c:set var="urlAtualizar" value="${ctx}/herds/edit"/>
<c:set var="urlExcluir" value="${ctx}/herds/delete"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rebanhos - Argos</title>

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
        <jsp:param name="ativo" value="rebanho"/>
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
                <h1>Rebanhos cadastrados</h1>

                <%-- FILTROS (futuro): o botão "Filtrar" do Figma entra aqui, antes do botão de novo registro --%>

                <button type="button" class="btn btn-escuro"
                        data-abrir="modalHerd" data-acao="criar" data-action="${urlInserir}"
                        data-titulo="Cadastrar Rebanho" data-botao="Cadastrar">
                    <i class="fa-solid fa-plus"></i> Novo rebanho
                </button>
            </div>

            <div class="busca">
                <i class="fa-solid fa-magnifying-glass"></i>
                <input type="search" id="campoBusca" placeholder="Buscar rebanho ..." aria-label="Buscar rebanho" autocomplete="off">
            </div>

            <div class="tabela-scroll">
                <table class="tabela" id="tabela">
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th>Raça</th>
                            <th>Finalidade</th>
                            <th>Quantidade de cabeças</th>
                            <th>Última atualização</th>
                            <th class="col-acoes">Ações</th>
                        </tr>
                    </thead>
                    <tbody>

                        <c:forEach var="herd" items="${herds}">
                            <tr class="linha">
                                <td><c:out value="${herd.name}"/></td>
                                <td><c:out value="${herd.breed}"/></td>
                                <td><c:out value="${herd.purpose}"/></td>
                                <td><c:out value="${herd.headCount}"/></td>
                                <td><c:out value="${herd.updatedAt}"/></td>
                                <td class="col-acoes">
                                    <button type="button" class="btn-icone btn-editar" title="Editar" aria-label="Editar rebanho"
                                            data-abrir="modalHerd" data-acao="editar" data-action="${urlAtualizar}"
                                            data-titulo="Editar Rebanho" data-botao="Salvar"
                                            data-id-herd="<c:out value='${herd.idHerd}'/>"
                                            data-name="<c:out value='${herd.name}'/>"
                                            data-breed="<c:out value='${herd.breed}'/>"
                                            data-purpose="<c:out value='${herd.purpose}'/>"
                                            data-head-count="<c:out value='${herd.headCount}'/>">
                                        <i class="fa-solid fa-pen"></i>
                                    </button>
                                    <button type="button" class="btn-icone btn-excluir" title="Excluir" aria-label="Excluir rebanho"
                                            data-abrir="modalExcluir"
                                            data-id-herd="<c:out value='${herd.idHerd}'/>"
                                            data-name="<c:out value='${herd.name}'/>">
                                        <i class="fa-solid fa-trash-can"></i>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>

                        <tr class="linha-vazia" id="linhaVazia" hidden>
                            <td colspan="6">Nenhum resultado encontrado.</td>
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
        <dialog class="modal" id="modalHerd">
            <div class="modal-caixa">
                <div class="modal-topo">
                    <h2>Cadastrar Rebanho</h2>
                    <button type="button" class="btn-fechar" data-fechar aria-label="Fechar"><i class="fa-solid fa-xmark"></i></button>
                </div>

                <form method="post" action="${urlInserir}">
                    <input type="hidden" name="idHerd" value="">

                    <div class="form-grade">
                        <div class="campo">
                            <label for="campoNome">Nome</label>
                            <input type="text" id="campoNome" name="name" placeholder="ex: Rebanho Alvorada"
                                   required maxlength="120" autocomplete="off">
                        </div>
                        <div class="campo">
                            <label for="campoRaca">Raça</label>
                            <input type="text" id="campoRaca" name="breed" placeholder="ex: Hereford"
                                   required maxlength="60" autocomplete="off">
                        </div>
                        <div class="campo">
                            <label for="campoFinalidade">Finalidade</label>
                            <input type="text" id="campoFinalidade" name="purpose" placeholder="ex: Corte"
                                   required maxlength="60" autocomplete="off">
                        </div>
                        <div class="campo">
                            <label for="campoQuantidade">Quantidade de cabeças</label>
                            <input type="number" id="campoQuantidade" name="headCount" placeholder="ex: 100"
                                   required min="0" step="1" inputmode="numeric" autocomplete="off">
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
                    <h2>Excluir Rebanho</h2>
                    <button type="button" class="btn-fechar" data-fechar aria-label="Fechar"><i class="fa-solid fa-xmark"></i></button>
                </div>

                <form method="post" action="${urlExcluir}">
                    <input type="hidden" name="idHerd" value="">

                    <p class="modal-texto">Tem certeza que deseja excluir <strong data-mostrar="name"></strong>?</p>

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
