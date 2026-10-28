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
        <jsp:param name="ativo" value="rebanho"/>
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
                <h1>Rebanhos cadastrados</h1>
<div class="painel-acoes">
                <c:set var="filtrando" value="${not empty param.name or not empty param.breed or not empty param.purpose or not empty param.propertyId}"/>
                <details class="filtro ${filtrando ? 'filtro-ativo' : ''}">
                    <summary>Filtrar</summary>
                    <form method="get" action="${ctx}/herds">
                            <div class="campo"><label for="filtro-name">Nome</label><input type="text" id="filtro-name" name="name" value="<c:out value='${param.name}'/>"/></div>
                            <div class="campo"><label for="filtro-breed">Raça</label><input type="text" id="filtro-breed" name="breed" value="<c:out value='${param.breed}'/>"/></div>
                            <div class="campo"><label for="filtro-purpose">Finalidade</label><input type="text" id="filtro-purpose" name="purpose" value="<c:out value='${param.purpose}'/>"/></div>
                            <c:if test="${not empty properties}"><div class="campo"><label for="filtro-propertyId">Propriedade</label><select id="filtro-propertyId" name="propertyId"><option value="">Todas</option><c:forEach var="property" items="${properties}"><option value="<c:out value='${property.idProperty}'/>" ${param.propertyId == property.idProperty ? 'selected="selected"' : ''}><c:out value="${property.name}"/></option></c:forEach></select></div></c:if>
                        <div class="filtro-acoes"><a href="${ctx}/herds" class="btn-pilula btn-neutro">Limpar</a><button type="submit" class="btn-pilula">Aplicar filtro</button></div>
                    </form>
                </details>
                <button type="button" class="btn btn-escuro"
                        data-abrir="modalHerd" data-acao="criar" data-action="${urlInserir}"
                        data-titulo="Cadastrar Rebanho" data-botao="Cadastrar">
                    <i class="fa-solid fa-plus"></i> Novo rebanho
                </button>
            </div>
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
                                <td><c:out value="${argos:formatDateTime(herd.updatedAt)}"/></td>
                                <td class="col-acoes">
                                    <button type="button" class="btn-icone btn-editar" title="Editar" aria-label="Editar rebanho"
                                            data-abrir="modalHerd" data-acao="editar" data-action="${urlAtualizar}"
                                            data-titulo="Editar Rebanho" data-botao="Salvar"
                                            data-id-herd="<c:out value='${herd.idHerd}'/>"
                                            data-name="<c:out value='${herd.name}'/>"
                                            data-breed="<c:out value='${herd.breed}'/>"
                                            data-purpose="<c:out value='${herd.purpose}'/>"
                                            data-head-count="<c:out value='${herd.headCount}'/>"
                                            data-property-id="<c:out value='${herd.propertyId}'/>">
                                        <i class="fa-solid fa-pen"></i>
                                    </button>
                                    <button type="button" class="btn-icone btn-excluir" title="Excluir" aria-label="Excluir rebanho"
                                            data-abrir="modalExcluir"
                                            data-id="<c:out value='${herd.idHerd}'/>"
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
                                   required maxlength="60" autocomplete="off">
                        </div>
                        <div class="campo">
                            <label for="campoRaca">Raça</label>
                            <input type="text" id="campoRaca" name="breed" placeholder="ex: Hereford"
                                   required maxlength="40" autocomplete="off">
                        </div>
                        <div class="campo">
                            <label for="campoFinalidade">Finalidade</label>
                            <select id="campoFinalidade" name="purpose" required>
                                <option value="Beef">Bovinos de corte (Beef)</option>
                            </select>
                        </div>
                        <div class="campo">
                            <label for="campoPropriedade">Propriedade</label>
                            <select id="campoPropriedade" name="propertyId" required><option value="">Selecione</option><c:if test="${not empty properties}"><c:forEach var="property" items="${properties}"><option value="<c:out value='${property.idProperty}'/>"><c:out value="${property.name}"/></option></c:forEach></c:if></select>
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
                    <input type="hidden" name="id" value="">

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
