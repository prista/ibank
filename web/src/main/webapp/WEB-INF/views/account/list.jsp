<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="mytags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="mytaglib" uri="my-custom-tags-uri" %>

<h4 class="header">Accounts</h4>

<c:set var="baseUrl" value="${pageContext.request.contextPath}/account"/>

<div class="row">
    <div class="col s12 m10">
        <div class="card-panel blue lighten-5">
            <div class="row">
                <form:form method="POST" action="${baseUrl}" modelAttribute="searchFormModel">
                    <div class="input-field col s4">
                        <form:input path="name" type="text"/>
                        <label for="name">name</label>
                    </div>
                    <div class="input-field col s4">
                        <div class="switch">
                            <label> all <form:checkbox path="unlockedOnly"/> <span class="lever"></span> unlocked only
                            </label>
                        </div>
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
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="name">name</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="userProfileUserName">User</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="accountType">accountType</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="balance">balance</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="currency">currency</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="locked">locked</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="bankName">bank</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="deleted">deleted</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="created">created</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="updated">updated</mytaglib:sort-link></th>
        <th></th>
    </tr>
    <c:forEach var="account" items="${listDTO.list}" varStatus="loopCounter">
        <tr>
            <td><c:out value="${account.id}"/></td>
            <td><c:out value="${account.name}"/></td>
            <td><c:out value="${account.userProfileUserName}"/></td>
            <td><c:out value="${account.accountType}"/></td>
            <td><c:out value="${account.balance}"/></td>
            <td><c:out value="${account.currency}"/></td>
            <td><c:out value="${account.locked}"/></td>
            <td><c:out value="${account.bankName}"/></td>
            <td><c:out value="${account.deleted}"/></td>
            <td><fmt:formatDate pattern="yyyy-MM-dd" value="${account.created}"/></td>
            <td><fmt:formatDate pattern="yyyy-MM-dd" value="${account.updated}"/></td>
            <td class="right">
                <a class="btn-floating" href="${baseUrl}/${account.id}"><i class="material-icons">info</i></a>
                <a class="btn-floating" href="${baseUrl}/${account.id}/edit"><i class="material-icons">edit</i></a>
                <c:choose>
                    <c:when test="${disabled}">
                        <a class="btn-floating red disabled" href="${baseUrl}/${account.id}/delete"><i
                                class="material-icons">delete</i></a>
                    </c:when>
                    <c:otherwise>
                        <a class="btn-floating red" href="${baseUrl}/${account.id}/delete"><i class="material-icons">delete</i></a>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<mytags:paging/>
<a class="waves-effect waves-light btn right" href="${baseUrl}/add"><i
        class="material-icons">add</i></a>
