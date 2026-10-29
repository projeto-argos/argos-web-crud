<%--
  Created by IntelliJ IDEA.
  User: Murilo Paiva
  Date: 17/09/2026
  Time: 12:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="argos" uri="/WEB-INF/argos.tld" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="urlInserir" value="${ctx}/properties/register"/>
<c:set var="urlAtualizar" value="${ctx}/properties/edit"/>
<c:set var="urlExcluir" value="${ctx}/properties/delete"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Propriedades - Argos</title>

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
        <jsp:param name="ativo" value="propriedade"/>
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
                <h1>Propriedades cadastradas</h1>

                <div class="painel-acoes">

                    <c:set var="filtrando" value="${not empty param.name or not empty param.cnpj or not empty param.phone or not empty param.userIds}"/>
                <details class="filtro ${filtrando ? 'filtro-ativo' : ''}">
                    <summary>Filtrar</summary>
                    <form method="get" action="${ctx}/properties">
                            <div class="campo"><label for="filtro-name">Nome</label><input type="text" id="filtro-name" name="name" value="<c:out value='${param.name}'/>"/></div>
                            <div class="campo"><label for="filtro-cnpj">CNPJ</label><input type="text" id="filtro-cnpj" name="cnpj" value="<c:out value='${param.cnpj}'/>"/></div>
                            <div class="campo"><label for="filtro-phone">Telefone</label><input type="text" id="filtro-phone" name="phone" value="<c:out value='${param.phone}'/>"/></div>
                            <c:if test="${not empty users}"><div class="campo"><label for="filtro-userIds">Usuário</label><select id="filtro-userIds" name="userIds"><option value="">Todos</option><c:forEach var="user" items="${users}"><option value="<c:out value='${user.idUser}'/>" ${param.userIds == user.idUser ? 'selected="selected"' : ''}><c:out value="${user.fullName}"/></option></c:forEach></select></div></c:if>
                        <div class="filtro-acoes"><a href="${ctx}/properties" class="btn-pilula btn-neutro">Limpar</a><button type="submit" class="btn-pilula">Aplicar filtro</button></div>
                    </form>
                </details>
                <button type="button" class="btn btn-escuro"
                        data-abrir="modalPropriedade" data-acao="criar" data-action="${urlInserir}"
                        data-titulo="Cadastrar Propriedade" data-botao="Cadastrar">
                    <i class="fa-solid fa-plus"></i> Nova propriedade
                </button>
            </div>
            </div>

            <div class="busca">
                <i class="fa-solid fa-magnifying-glass"></i>
                <input type="search" id="campoBusca" placeholder="Buscar propriedade ..." aria-label="Buscar propriedade" autocomplete="off">
            </div>

            <div class="tabela-scroll">
                <table class="tabela" id="tabela">
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th>CNPJ</th>
                            <th>Telefone</th>
                            <th>Última atualização</th>
                            <th class="col-acoes">Ações</th>
                        </tr>
                    </thead>
                    <tbody>

                        <c:forEach var="property" items="${properties}">
                            <tr class="linha" data-filter-name="<c:out value='${property.name}'/>" data-filter-cnpj="<c:out value='${property.cnpj}'/>" data-filter-phone="<c:out value='${property.phone}'/>">
                                <td><c:out value="${property.name}"/></td>
                                <td class="cnpj"><c:out value="${property.cnpj}"/></td>
                                <td class="telefone"><c:out value="${property.phone}"/></td>
                                <td><c:out value="${argos:formatDateTime(property.updatedAt)}"/></td>
                                <td class="col-acoes">
                                    <button type="button" class="btn-icone btn-editar" title="Editar" aria-label="Editar propriedade"
                                            data-abrir="modalPropriedade" data-acao="editar" data-action="${urlAtualizar}"
                                            data-titulo="Editar Propriedade" data-botao="Salvar"
                                            data-id-property="<c:out value='${property.idProperty}'/>"
                                            data-name="<c:out value='${property.name}'/>"
                                            data-cnpj="<c:out value='${property.cnpj}'/>"
                                            data-phone="<c:out value='${property.phone}'/>"
                                            data-street="<c:out value='${propertyAddresses[property.idProperty].street}'/>"
                                            data-number="<c:out value='${propertyAddresses[property.idProperty].number}'/>"
                                            data-city="<c:out value='${propertyAddresses[property.idProperty].city}'/>"
                                            data-state="<c:out value='${propertyAddresses[property.idProperty].state}'/>"
                                            data-zip-code="<c:out value='${propertyAddresses[property.idProperty].zipCode}'/>"
                                            data-complement="<c:out value='${propertyAddresses[property.idProperty].complement}'/>"
                                            data-user-ids="<c:forEach var='uid' items='${property.userIds}' varStatus='st'><c:out value='${uid}'/><c:if test='${not st.last}'>,</c:if></c:forEach>">
                                        <i class="fa-solid fa-pen"></i>
                                    </button>
                                    <button type="button" class="btn-icone btn-excluir" title="Excluir" aria-label="Excluir propriedade"
                                            data-abrir="modalExcluir"
                                            data-id="<c:out value='${property.idProperty}'/>"
                                            data-name="<c:out value='${property.name}'/>">
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
        <dialog class="modal" id="modalPropriedade">
            <div class="modal-caixa">
                <div class="modal-topo">
                    <h2>Cadastrar Propriedade</h2>
                    <button type="button" class="btn-fechar" data-fechar aria-label="Fechar"><i class="fa-solid fa-xmark"></i></button>
                </div>

                <form method="post" action="${urlInserir}">
                    <input type="hidden" name="idProperty" value="">

                    <div class="form-grade">
                        <div class="campo">
                            <label for="campoNome">Nome</label>
                            <input type="text" id="campoNome" name="name" placeholder="ex: Recanto do Vale"
                                   required maxlength="120" autocomplete="off">
                        </div>
                        <div class="campo">
                            <label for="campoTelefone">Telefone</label>
                            <input type="tel" id="campoTelefone" name="phone" placeholder="ex: 11999999999"
                                   required inputmode="numeric" maxlength="11" pattern="\d{10,11}"
                                   title="DDD + número, só números (10 ou 11 dígitos)" autocomplete="off">
                        </div>
                        <div class="campo">
                            <label for="campoCnpj">CNPJ</label>
                            <input type="text" id="campoCnpj" name="cnpj" placeholder="ex: 12345678901234"
                                   required inputmode="numeric" maxlength="14" pattern="\d{14}"
                                   title="Digite os 14 números do CNPJ, sem pontos, barra e traço" autocomplete="off">
                        </div>
                        <div class="campo">
                            <label for="campoCep">CEP</label>
                            <input type="text" id="campoCep" name="zipCode" placeholder="ex: 14000000"
                                   inputmode="numeric" maxlength="8" pattern="\d{8}"
                                   title="Digite os 8 números do CEP, sem traço" autocomplete="off">
                        </div>
                        <div class="campo">
                            <label for="campoEstado">Estado</label>
                            <input type="text" id="campoEstado" name="state" placeholder="ex: SP"
                                   maxlength="2" pattern="[A-Za-z]{2}" title="Sigla do estado, 2 letras" autocomplete="off">
                        </div>
                        <div class="campo">
                            <label for="campoCidade">Cidade</label>
                            <input type="text" id="campoCidade" name="city" placeholder="ex: Ribeirão Preto"
                                   maxlength="100" autocomplete="off">
                        </div>
                        <div class="campo">
                            <label for="campoRua">Rua</label>
                            <input type="text" id="campoRua" name="street" placeholder="ex: Estrada do Vale"
                                   maxlength="150" autocomplete="off">
                        </div>
                        <div class="campo">
                            <label for="campoNumero">Número</label>
                            <input type="number" id="campoNumero" name="number" placeholder="ex: 120" min="0">
                        </div>
                        <div class="campo largura-total">
                            <label for="campoComplemento">Complemento</label>
                            <input type="text" id="campoComplemento" name="complement" placeholder="ex: Km 12, portão azul"
                                   autocomplete="off">
                        </div>
                    </div>

                    <h3 class="form-secao">Usuário</h3>
                    <div class="form-grade">
                        <div class="campo largura-total">
                            <label for="campoUsuario">Usuário</label>
                            <select id="campoUsuario" name="userIds" required>
                                <option value="">Selecione</option>
                                <c:forEach var="user" items="${users}">
                                    <option value="<c:out value='${user.idUser}'/>"><c:out value="${user.fullName}"/></option>
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
                    <h2>Excluir Propriedade</h2>
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
