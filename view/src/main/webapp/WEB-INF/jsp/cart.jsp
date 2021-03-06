<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<t:frontendLayout>
    <jsp:attribute name="content">

        <h1 class="">Your cart!</h1>
        <c:if test="${isCartEmpty}">
            <div>
                <spring:message code="cart.empty"/>
            </div>
        </c:if>
        <c:if test="${!isCartEmpty}">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Product name</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Cost</th>
                        <th>Remove</th>
                    </tr>
                </thead>

                <tbody>
                    <c:forEach var="item" items="${sessionScope.cart.items}">
                        <tr product-id="${item.product.id}">
                            <td>${item.product.name}</td>
                            <td><span class="product_price">${item.product.price}</span> $</td>
                            <td>
                                <label>
                                    <input type="number" class="cart_product_quantity" name="cart_quantity" value="${item.quantity}">
                                </label>
                            </td>
                            <td>
                                <span class="product_cost">${item.product.price * item.quantity}</span> $
                            </td>
                            <td><button type="button" class="btn btn-default cart_product_remove"><span class="glyphicon glyphicon-trash"></span></button></td>
                        </tr>
                    </c:forEach>

                </tbody>
                <tfoot>
                    <tr>
                        <td></td>
                        <td></td>
                        <td>Total cost:</td>
                        <td colspan="2"><span class="cart_summary">${sessionScope.cart.summary}</span> $</td>
                    </tr>
                <tr>
                    <td>
                        <a class="btn btn-primary" href="<c:url value="/checkout"/>">Proceed to checkout</a>
                    </td>
                </tr>
                </tfoot>
            </table>
        </c:if>
    </jsp:attribute>

</t:frontendLayout>
