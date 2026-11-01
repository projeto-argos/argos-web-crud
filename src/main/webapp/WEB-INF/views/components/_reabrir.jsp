<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${pageContext.request.method == 'POST' and not empty erro}">
    <div id="valoresErro" hidden>
        <c:forEach var="entrada" items="${paramValues}">
            <c:if test="${entrada.key != 'password'}">
                <c:forEach var="valor" items="${entrada.value}">
                    <span data-nome="<c:out value='${entrada.key}'/>" data-valor="<c:out value='${valor}'/>"></span>
                </c:forEach>
            </c:if>
        </c:forEach>
    </div>
</c:if>
