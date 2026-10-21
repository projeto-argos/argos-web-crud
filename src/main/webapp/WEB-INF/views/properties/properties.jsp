<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

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
    <link rel="stylesheet" href="${ctx}/assets/css/feedbackMessages.css">

    <!-- JAVASCRIPT -->
    <script src="${ctx}/assets/js/sidebar.js"></script>
    <script src="${ctx}/assets/js/popup.js" defer></script>
    <script src="${ctx}/assets/js/tabela.js" defer></script>
</head>
<body>

    <jsp:include page="/WEB-INF/views/components/_sidebar.jsp">
        <jsp:param name="ativo" value="propriedade"/>
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
                <h1>Propriedades cadastradas</h1>

                <%-- FILTROS (futuro): o botão "Filtrar" do Figma entra aqui, antes do botão de novo registro --%>

                <button type="button" class="btn btn-escuro"
                        data-abrir="modalPropriedade" data-acao="criar" data-action="${urlInserir}"
                        data-titulo="Cadastrar Propriedade" data-botao="Cadastrar">
                    <i class="fa-solid fa-plus"></i> Nova propriedade
                </button>
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
                            <tr class="linha">
                                <td><c:out value="${property.name}"/></td>
                                <td class="cnpj"><c:out value="${property.cnpj}"/></td>
                                <td class="telefone"><c:out value="${property.phone}"/></td>
                                <td><c:out value="${property.updatedAt}"/></td>
                                <td class="col-acoes">
                                    <button type="button" class="btn-icone btn-editar" title="Editar" aria-label="Editar propriedade"
                                            data-abrir="modalPropriedade" data-acao="editar" data-action="${urlAtualizar}"
                                            data-titulo="Editar Propriedade" data-botao="Salvar"
                                            data-id-property="<c:out value='${property.idProperty}'/>"
                                            data-name="<c:out value='${property.name}'/>"
                                            data-cnpj="<c:out value='${property.cnpj}'/>"
                                            data-phone="<c:out value='${property.phone}'/>">
                                        <i class="fa-solid fa-pen"></i>
                                    </button>
                                    <button type="button" class="btn-icone btn-excluir" title="Excluir" aria-label="Excluir propriedade"
                                            data-abrir="modalExcluir"
                                            data-id-property="<c:out value='${property.idProperty}'/>"
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
                            <label for="campoCnpj">CNPJ</label>
                            <input type="text" id="campoCnpj" name="cnpj" placeholder="ex: 12345678901234"
                                   required inputmode="numeric" maxlength="14" pattern="\d{14}"
                                   title="Digite os 14 números do CNPJ, sem pontos, barra e traço" autocomplete="off">
                        </div>
                        <div class="campo">
                            <label for="campoTelefone">Telefone</label>
                            <input type="tel" id="campoTelefone" name="phone" placeholder="ex: 11999999999"
                                   required inputmode="numeric" maxlength="11" pattern="\d{10,11}"
                                   title="DDD + número, só números (10 ou 11 dígitos)" autocomplete="off">
                        </div>
                        <div class="campo largura-total">
                            <label for="campoNome">Nome</label>
                            <input type="text" id="campoNome" name="name" placeholder="ex: Recanto do Vale"
                                   required maxlength="120" autocomplete="off">
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
                    <input type="hidden" name="idProperty" value="">

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
