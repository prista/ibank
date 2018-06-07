<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h4 class="header">Edit Payment Type</h4>

<c:set var="baseUrl" value="${pageContext.request.contextPath}/payment-type"/>

<div class="row">
    <form:form class="col s12" method="POST" action="${baseUrl}/add" modelAttribute="formModel">
        <form:input path="id" type="hidden"/>

        <div class="row">
            <div class="input-field col s12">
                <form:select path="parentId" disabled="${readonly}">
                    <form:options items="${parentChoices}"/>
                </form:select>
                <form:errors path="parentId" cssClass="red-text"/>
                <label for="parentId">parent</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <form:input path="name" type="text" disabled="${readonly}"/>
                <form:errors path="name" cssClass="red-text"/>
                <label for="name">name</label>
            </div>
        </div>

        <div class="row">
            <div class="col s6"></div>
            <div class="col s3">
                <c:if test="${!readonly}">
                    <button class="btn waves-effect waves-light right" type="submit">сохранить</button>
                </c:if>
            </div>
            <div class="col s3">
                <a class="btn waves-effect waves-light right" href="${baseUrl}">к списку<i
                        class="material-icons right"></i>
                </a>
            </div>
        </div>
    </form:form>
</div>
