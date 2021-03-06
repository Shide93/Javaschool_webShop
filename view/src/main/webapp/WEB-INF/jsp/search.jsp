<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:frontendLayout>
    <jsp:attribute name="content">
        <h1>Search products</h1>
         <div class="panel panel-default">
            <form action="<c:url value="/search"/>" method="post">
                <c:forEach var="feature" items="${requestScope.features}">
                    <div class="panel panel-default">
                        <div class="panel-heading">${feature.name}</div>
                        <div class="panel-body">
                            <c:forEach var="fValue" items="${requestScope.featureValues}">
                                <c:if test="${fValue.featureId eq feature.id}">
                                    <div class="checkbox">
                                        <label>
                                            <input type="checkbox" name="features" value="${feature.id}/${fValue.value}">
                                            ${fValue.value}
                                        </label>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>
                <input type="submit" class="btn btn-primary" value="Search">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </div>
    </jsp:attribute>

</t:frontendLayout>
