<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="mytags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="mytaglib" uri="my-custom-tags-uri" %>

<h4 class="header">Transactions</h4>

<c:set var="baseUrl" value="${pageContext.request.contextPath}/transaction"/>

<div class="row">
    <div class="col s12 m10">
        <div class="card-panel blue lighten-5">
            <div class="row">
                <form:form method="POST" action="${baseUrl}" modelAttribute="searchFormModel">
                    <div class="input-field col s4">
                        <form:input path="note" type="text"/>
                        <label for="note">note</label>
                    </div>
                    <div class="col s4">
                        <button class="btn waves-effect waves-light right" type="submit">search</button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<table class="bordered highlight">
    <tbody>
    <tr>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="id">id</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="accountFromName">from</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="accountToName">to</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="amount">amount</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="note">note</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="paymentTypeName">paymentType</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="transactionType">transactionType</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="created">created</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="updated">updated</mytaglib:sort-link></th>
        <th></th>
    </tr>
    <c:forEach var="transaction" items="${listDTO.list}" varStatus="loopCounter">
        <tr>
            <td><c:out value="${transaction.id}"/></td>
            <td><c:out value="${transaction.accountFromName}"/></td>
            <td><c:out value="${transaction.accountToName}"/></td>
            <td><c:out value="${transaction.amount}"/></td>
            <td><c:out value="${transaction.note}"/></td>
            <td><c:out value="${transaction.paymentTypeName}"/></td>
            <td><c:out value="${transaction.transactionType}"/></td>
            <td><fmt:formatDate pattern="yyyy-MM-dd" value="${transaction.created}"/></td>
            <td><fmt:formatDate pattern="yyyy-MM-dd" value="${transaction.updated}"/></td>
            <td class="right">
                <a class="btn-floating" href="${baseUrl}/${transaction.id}"><i class="material-icons">info</i></a>
                <a class="btn-floating" href="${baseUrl}/${transaction.id}/edit"><i class="material-icons">edit</i></a>
                <c:choose>
                    <c:when test="${disabled}">
                        <a class="btn-floating red disabled" href="${baseUrl}/${transaction.id}/delete"><i
                                class="material-icons">delete</i></a>
                    </c:when>
                    <c:otherwise>
                        <a class="btn-floating red" href="${baseUrl}/${transaction.id}/delete"><i
                                class="material-icons">delete</i></a>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<mytags:paging/>
<a class="waves-effect waves-light btn right" href="${baseUrl}/add"><i class="material-icons">add</i></a>

<script>
    var latestId = '${newestTransactionId}';
    /*debugger;*/
    if (latestId) { // if latestId is null or undefined , then false
        setInterval(function () {
            $.get("${baseUrl}/lastId", function (lastIdFromServer) {

                if (latestId < lastIdFromServer) {
                    M.toast({html: 'Someone created newest Transaction!'}) // simple popup message using Materialize framework
                    latestId = lastIdFromServer;
                }
            })
        }, 5 * 1000);
    }
</script>

