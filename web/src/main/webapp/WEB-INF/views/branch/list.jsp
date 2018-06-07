<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="mytags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="mytaglib" uri="my-custom-tags-uri" %>

<h4 class="header">Branches</h4>

<c:set var="baseUrl" value="${pageContext.request.contextPath}/branch"/>

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
                        <form:input path="bankName" type="text"/>
                        <label for="bankName">bank</label>
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
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="bankName">bankName</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="city">city</mytaglib:sort-link></th>
        <th>street address</th>
        <th>post code</th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="created">created</mytaglib:sort-link></th>
        <th><mytaglib:sort-link pageUrl="${baseUrl}" column="updated">updated</mytaglib:sort-link></th>
        <th></th>
    </tr>
    <c:forEach var="branch" items="${listDTO.list}" varStatus="loopCounter">
        <tr>
            <td><c:out value="${branch.id}"/></td>
            <td><c:out value="${branch.name}"/></td>
            <td><c:out value="${branch.bankName}"/></td>
            <td><c:out value="${branch.city}"/></td>
            <td><c:out value="${branch.streetAddress}"/></td>
            <td><c:out value="${branch.postCode}"/></td>
            <td><fmt:formatDate pattern="yyyy-MM-dd" value="${branch.created}"/></td>
            <td><fmt:formatDate pattern="yyyy-MM-dd" value="${branch.updated}"/></td>
            <td class="right">
                <a class="btn-floating" href="${baseUrl}/${branch.id}"><i class="material-icons">info</i></a>
                <a class="btn-floating" href="${baseUrl}/${branch.id}/edit"><i class="material-icons">edit</i></a>
                <a class="btn-floating red" href="${baseUrl}/${branch.id}/delete"><i
                        class="material-icons">delete</i></a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<mytags:paging/>
<a class="waves-effect waves-light btn right" href="${baseUrl}/add"><i class="material-icons">add</i></a>

