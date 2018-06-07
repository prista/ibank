<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h4 class="header">Edit branch</h4>

<c:set var="baseUrl" value="${pageContext.request.contextPath}/branch"/>

<div class="row">
    <form:form class="col s12" method="POST" action="${baseUrl}/add" modelAttribute="formModel">
        <form:input path="id" type="hidden"/>
        <div class="row">
            <div class="input-field col s12">
                <form:input path="name" type="text" disabled="${readonly}"/>
                <form:errors path="name" cssClass="red-text"/>
                <label for="name">название</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <form:select path="bankId" disabled="${readonly}">
                    <form:options items="${banksChoices}"/>
                </form:select>
                <form:errors path="bankId" cssClass="red-text"/>
                <label for="bankId">bank</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <form:input path="city" type="text" disabled="${readonly}"/>
                <form:errors path="city" cssClass="red-text"/>
                <label for="name">city</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <form:input path="streetAddress" type="text" disabled="${readonly}"/>
                <form:errors path="streetAddress" cssClass="red-text"/>
                <label for="name">street address</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <form:input path="postCode" type="text" disabled="${readonly}"/>
                <form:errors path="postCode" cssClass="red-text"/>
                <label for="name">post code</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field  col s12">
                <form:select path="userProfilesIds" disabled="${readonly}" multiple="true">
                    <option value="" disabled selected>выберите сотрудников</option>
                    <form:options items="${userProfilesChoices}"/>
                </form:select>
                <form:errors path="userProfilesIds" cssClass="red-text"/>
                <label for="userProfilesIds" class="multiselect-label">Сотрудники</label>
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
