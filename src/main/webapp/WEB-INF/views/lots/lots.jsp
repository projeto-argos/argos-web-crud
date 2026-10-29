<%--
  Created by IntelliJ IDEA.
  User: Murilo Paiva
  Date: 17/09/2026
  Time: 12:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="argos" uri="/WEB-INF/argos.tld" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="urlInserir" value="${ctx}/lots/register"/>
<c:set var="urlAtualizar" value="${ctx}/lots/edit"/>
<c:set var="urlExcluir" value="${ctx}/lots/delete"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lotes - Argos</title>

    <!-- Fontes do Figma  -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;700&family=Rubik:wght@500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <!-- CSS -->
    <link rel="stylesheet" href="${ctx}/assets/css/sidebar.css">
    <link rel="stylesheet" href="${ctx}/assets/css/tables.css">
    <link rel="stylesheet" href="${ctx}/assets/css/forms.css">
    <link rel="stylesheet" href="${ctx}/assets/css/filtro.css">
    <link rel="stylesheet" href="${ctx}/assets/css/feedbackMessages.css">

    <!-- JAVASCRIPT -->
    <script src="${ctx}/assets/js/sidebar.js"></script>
    <script src="${ctx}/assets/js/popup.js" defer></script>
    <script src="${ctx}/assets/js/tabela.js" defer></script>
    <script src="${ctx}/assets/js/filtro.js" defer></script>
</head>
<body>

<jsp:include page="/WEB-INF/views/components/_sidebar.jsp">
    <jsp:param name="ativo" value="lot"/>
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

    <section class="painel">

        <div class="painel-topo">
            <h1>Lotes cadastrados</h1>

            <c:set var="filtrando" value="${not empty param.lotCode or not empty param.herdId or not empty param.category or not empty param.headCount or not empty param.openDate}"/>
            <div class="painel-acoes">
            <details class="filtro ${filtrando ? 'filtro-ativo' : ''}">
                <summary>Filtrar</summary>
                <form method="get" action="${ctx}/lots">
                    <div class="campo"><label for="filtro-lotCode">Código lote</label><input type="text" id="filtro-lotCode" name="lotCode" value="<c:out value='${param.lotCode}'/>"/></div>
                    <c:if test="${not empty herds}">
                        <div class="campo">
                            <label for="filtro-herdId">Rebanho</label>
                            <select id="filtro-herdId" name="herdId">
                                <option value="">Todos</option>
                                <c:forEach var="herd" items="${herds}">
                                    <option value="<c:out value='${herd.idHerd}'/>" ${param.herdId == herd.idHerd ? 'selected' : ''}><c:out value="${herd.name}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                    </c:if>
                    <div class="campo">
                        <label for="filtro-category">Categoria</label>
                        <select id="filtro-category" name="category">
                            <option value="">Todas</option>
                            <option value="BREEDING" ${param.category == 'BREEDING' ? 'selected' : ''}>Cria</option>
                            <option value="REARING" ${param.category == 'REARING' ? 'selected' : ''}>Recria</option>
                            <option value="FATTENING" ${param.category == 'FATTENING' ? 'selected' : ''}>Engorda</option>
                        </select>
                    </div>
                    <div class="campo"><label for="filtro-headCount">Quantidade de cabeças</label><input type="number" id="filtro-headCount" name="headCount" min="0" value="<c:out value='${param.headCount}'/>"/></div>
                    <div class="campo"><label for="filtro-openDate">Data de abertura</label><input type="date" id="filtro-openDate" name="openDate" value="<c:out value='${param.openDate}'/>"/></div>
                    <div class="filtro-acoes"><a href="${ctx}/lots" class="btn-pilula btn-neutro">Limpar</a><button type="submit" class="btn-pilula">Aplicar filtro</button></div>
                </form>
            </details>
            <button type="button" class="btn btn-escuro"
                    data-abrir="modalLote" data-acao="criar" data-action="${urlInserir}"
                    data-titulo="Cadastrar Lote" data-botao="Cadastrar">
                <i class="fa-solid fa-plus"></i> Novo lote
            </button>
            </div>
        </div>

        <div class="busca">
            <i class="fa-solid fa-magnifying-glass"></i>
            <input type="search" id="campoBusca" placeholder="Buscar lote ..." aria-label="Buscar lote" autocomplete="off">
        </div>

        <div class="tabela-scroll">
            <table class="tabela" id="tabela">
                <thead>
                <tr>
                    <th>Código lote</th>
                    <th>Rebanho</th>
                    <th>Categoria</th>
                    <th>Quantidade de cabeças</th>
                    <th>Data de abertura</th>
                    <th>Última atualização</th>
                    <th class="col-acoes">Ações</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="lot" items="${lots}">
                    <tr class="linha">
                        <td><c:out value="${lot.lotCode}"/></td>
                        <td><c:forEach var="herd" items="${herds}"><c:if test="${herd.idHerd == lot.herdId}"><c:out value="${herd.name}"/></c:if></c:forEach></td>
                        <td>
                            <c:choose>
                                <c:when test="${lot.category == 'BREEDING'}">Cria</c:when>
                                <c:when test="${lot.category == 'REARING'}">Recria</c:when>
                                <c:when test="${lot.category == 'FATTENING'}">Engorda</c:when>
                                <c:otherwise><c:out value="${lot.category}"/></c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:out value="${lot.headCount}"/></td>
                        <td><c:out value="${lot.openDate}"/></td>
                        <td class="col-data"><c:out value="${argos:formatDateTime(lot.updatedAt)}"/></td>
                        <td class="col-acoes">
                            <button type="button" class="btn-icone btn-editar" title="Editar" aria-label="Editar lote"
                                    data-abrir="modalLote" data-acao="editar" data-action="${urlAtualizar}"
                                    data-titulo="Editar Lote" data-botao="Salvar"
                                    data-id-lot="<c:out value='${lot.idLot}'/>"
                                    data-herd-id="<c:out value='${lot.herdId}'/>"
                                    data-lot-code="<c:out value='${lot.lotCode}'/>"
                                    data-category="<c:out value='${lot.category}'/>"
                                    data-head-count="<c:out value='${lot.headCount}'/>"
                                    data-open-date="<c:out value='${lot.openDate}'/>">
                                <i class="fa-solid fa-pen"></i>
                            </button>
                            <button type="button" class="btn-icone btn-excluir" title="Excluir" aria-label="Excluir lote"
                                    data-abrir="modalExcluir"
                                    data-id="<c:out value='${lot.idLot}'/>"
                                    data-lot-code="<c:out value='${lot.lotCode}'/>">
                                <i class="fa-solid fa-trash-can"></i>
                            </button>
                        </td>
                    </tr>
                </c:forEach>

                <tr class="linha-vazia" id="linhaVazia" hidden>
                    <td colspan="7">Nenhum resultado encontrado.</td>
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
    <dialog class="modal" id="modalLote">
        <div class="modal-caixa">
            <div class="modal-topo">
                <h2>Cadastrar Lote</h2>
                <button type="button" class="btn-fechar" data-fechar aria-label="Fechar"><i class="fa-solid fa-xmark"></i></button>
            </div>

            <form method="post" action="${urlInserir}">
                <input type="hidden" name="idLot" value="">

                <div class="form-grade">
                    <div class="campo">
                        <label for="campoCodigo">Código do Lote</label>
                        <input type="text" id="campoCodigo" name="lotCode" placeholder="ex: LOTE-001"
                               required maxlength="20" autocomplete="off">
                    </div>
                    <div class="campo">
                        <label for="campoCategoria">Categoria</label>
                        <select id="campoCategoria" name="category" required>
                            <option value="" disabled selected>Selecione a categoria</option>
                            <option value="BREEDING">Cria</option>
                            <option value="REARING">Recria</option>
                            <option value="FATTENING">Engorda</option>
                        </select>
                    </div>
                    <div class="campo">
                        <label for="campoQuantidade">Quantidade de Cabeças</label>
                        <input type="number" id="campoQuantidade" name="headCount" placeholder="ex: 10"
                               required min="0" step="1" inputmode="numeric" autocomplete="off">
                    </div>
                    <div class="campo">
                        <label for="campoDataAbertura">Data de Abertura</label>
                        <input type="date" id="campoDataAbertura" name="openDate" required>
                    </div>
                    <div class="campo largura-total">
                        <label for="campoRebanho">Rebanho</label>
                        <select id="campoRebanho" name="herdId" required>
                            <option value="" disabled selected>Selecione o rebanho</option>
                            <c:forEach var="herd" items="${herds}">
                                <option value="<c:out value='${herd.idHerd}'/>"><c:out value="${herd.name}"/></option>
                            </c:forEach>
                        </select>
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
                <h2>Excluir Lote</h2>
                <button type="button" class="btn-fechar" data-fechar aria-label="Fechar"><i class="fa-solid fa-xmark"></i></button>
            </div>

            <form method="post" action="${urlExcluir}">
                <input type="hidden" name="id" value="">

                <p class="modal-texto">Tem certeza que deseja excluir o lote <strong data-mostrar="lotCode"></strong>?</p>

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
