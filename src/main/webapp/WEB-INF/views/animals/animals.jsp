<%--
    Created by IntelliJ IDEA.
    User: Murilo Paiva
    Date: 17/09/2026
    Time: 12:02
    To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="argos" uri="/WEB-INF/argos.tld" %>

<fmt:setLocale value="pt_BR"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="urlInserir" value="${ctx}/animals/register"/>
<c:set var="urlAtualizar" value="${ctx}/animals/edit"/>
<c:set var="urlExcluir" value="${ctx}/animals/delete"/>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Animais - Argos</title>

    <!-- Fontes do Figma -->
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
    <jsp:param name="ativo" value="animal"/>
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
            <h1>Animais cadastrados</h1>
            <div class="painel-acoes">
                <c:set var="filtrando" value="${not empty param.earTag or not empty param.batchId or not empty param.exceptionReason}"/>
                <details class="filtro ${filtrando ? 'filtro-ativo' : ''}">
                    <summary>Filtrar</summary>
                    <form method="get" action="${ctx}/animals">
                            <div class="campo"><label for="filtro-earTag">Brinco</label><input type="text" id="filtro-earTag" name="earTag" value="<c:out value='${param.earTag}'/>"/></div>
                            <c:if test="${not empty lots}"><div class="campo"><label for="filtro-batchId">Lote</label><select id="filtro-batchId" name="batchId"><option value="">Todos</option><c:forEach var="lot" items="${lots}"><option value="<c:out value='${lot.idLot}'/>" ${param.batchId == lot.idLot ? 'selected="selected"' : ''}><c:out value="${lot.lotCode}"/></option></c:forEach></select></div></c:if>
                            <div class="campo"><label for="filtro-exceptionReason">Motivo da exceção</label><input type="text" id="filtro-exceptionReason" name="exceptionReason" value="<c:out value='${param.exceptionReason}'/>"/></div>
                        <div class="filtro-acoes"><a href="${ctx}/animals" class="btn-pilula btn-neutro">Limpar</a><button type="submit" class="btn-pilula">Aplicar filtro</button></div>
                    </form>
                </details>
                <button type="button" class="btn btn-escuro"
                    data-abrir="modalAnimal" data-acao="criar" data-action="${urlInserir}"
                    data-titulo="Cadastrar Animal" data-botao="Cadastrar">
                <i class="fa-solid fa-plus"></i> Novo animal
            </button>
        </div>
            </div>

        <div class="busca">
            <i class="fa-solid fa-magnifying-glass"></i>
            <input type="search" id="campoBusca" placeholder="Buscar animal ..." aria-label="Buscar animal" autocomplete="off">
        </div>

        <div class="tabela-scroll">
            <table class="tabela" id="tabela">
                <thead>
                <tr>
                    <th>Brinco</th>
                    <th>Peso (kg)</th>
                    <th>Data de nascimento</th>
                    <th>Data início exceção</th>
                    <th>Data fim exceção</th>
                    <th>Motivo exceção</th>
                    <th class="col-acoes">Ações</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="animal" items="${animals}">
                    <tr class="linha">
                        <td><c:out value="${animal.earTag}"/></td>
                        <td><fmt:formatNumber value="${animal.weight}" minFractionDigits="2" maxFractionDigits="2"/></td>
                        <td><c:out value="${argos:formatDate(animal.birthDate)}"/></td>
                        <td><c:out value="${argos:formatDate(animal.exceptionStartDate)}"/></td>
                        <td><c:out value="${argos:formatDate(animal.exceptionEndDate)}"/></td>
                        <td><c:out value="${animal.exceptionReason}"/></td>
                        <td class="col-acoes">
                            <button type="button" class="btn-icone btn-editar" title="Editar" aria-label="Editar animal"
                                    data-abrir="modalAnimal" data-acao="editar" data-action="${urlAtualizar}"
                                    data-titulo="Editar Animal" data-botao="Salvar"
                                    data-id-animal="<c:out value='${animal.idAnimal}'/>"
                                    data-ear-tag="<c:out value='${animal.earTag}'/>"
                                    data-weight="<c:out value='${animal.weight}'/>"
                                    data-exception-reason="<c:out value='${animal.exceptionReason}'/>"
                                    data-notes="<c:out value='${animal.notes}'/>"
                                    data-birth-date="<c:out value='${animal.birthDate}'/>"
                                    data-exception-start-date="<c:out value='${animal.exceptionStartDate}'/>"
                                    data-exception-end-date="<c:out value='${animal.exceptionEndDate}'/>"
                                    data-batch-id="<c:out value='${animal.batchId}'/>">
                                <i class="fa-solid fa-pen"></i>
                            </button>
                            <button type="button" class="btn-icone btn-excluir" title="Excluir" aria-label="Excluir animal"
                                    data-abrir="modalExcluir"
                                    data-id="<c:out value='${animal.idAnimal}'/>"
                                    data-ear-tag="<c:out value='${animal.earTag}'/>">
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
    <dialog class="modal" id="modalAnimal">
        <div class="modal-caixa">
            <div class="modal-topo">
                <h2>Cadastrar Animal</h2>
                <button type="button" class="btn-fechar" data-fechar aria-label="Fechar"><i class="fa-solid fa-xmark"></i></button>
            </div>

            <form method="post" action="${urlInserir}">
                <input type="hidden" name="idAnimal" value="">

                <div class="form-grade">
                    <div class="campo">
                        <label for="campoBrinco">Brinco</label>
                        <input type="text" id="campoBrinco" name="earTag" placeholder="ex: BR-26-0001"
                               required maxlength="30" autocomplete="off">
                    </div>
                    <div class="campo">
                        <label for="campoLote">Lote</label>
                        <select id="campoLote" name="batchId" required><option value="">Selecione</option><c:if test="${not empty lots}"><c:forEach var="lot" items="${lots}"><option value="<c:out value='${lot.idLot}'/>"><c:out value="${lot.lotCode}"/></option></c:forEach></c:if></select>
                    </div>
                    <div class="campo">
                        <label for="campoPeso">Peso</label>
                        <input type="number" id="campoPeso" name="weight" placeholder="ex: 400.00"
                               required min="0" step="0.01" inputmode="decimal" autocomplete="off">
                    </div>
                    <div class="campo largura-total">
                        <label for="campoMotivo">Motivo exceção</label>
                        <input type="text" id="campoMotivo" name="exceptionReason" placeholder="ex: Parainfluenza Bovino (PI-3)"
                               maxlength="120" autocomplete="off">
                    </div>
                    <div class="campo largura-total">
                        <label for="campoObservacoes">Observações</label>
                        <input type="text" id="campoObservacoes" name="notes" placeholder="ex: Vírus destrói pulmões de bezerros."
                               maxlength="255" autocomplete="off">
                    </div>
                    <div class="campo">
                        <label for="campoNascimento">Data de Nascimento</label>
                        <input type="date" id="campoNascimento" name="birthDate" required>
                    </div>
                    <div class="campo">
                        <label for="campoInicioExcecao">Data início exceção</label>
                        <input type="date" id="campoInicioExcecao" name="exceptionStartDate">
                    </div>
                    <div class="campo">
                        <label for="campoFimExcecao">Data final da exceção</label>
                        <input type="date" id="campoFimExcecao" name="exceptionEndDate">
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
                <h2>Excluir Animal</h2>
                <button type="button" class="btn-fechar" data-fechar aria-label="Fechar"><i class="fa-solid fa-xmark"></i></button>
            </div>

            <form method="post" action="${urlExcluir}">
                <input type="hidden" name="id" value="">

                <p class="modal-texto">Tem certeza que deseja excluir o animal <strong data-mostrar="earTag"></strong>?</p>

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

