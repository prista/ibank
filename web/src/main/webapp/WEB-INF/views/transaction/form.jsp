<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h4 class="header">Edit transaction</h4>

<c:set var="baseUrl" value="${pageContext.request.contextPath}/transaction"/>

<div class="row">
    <form:form class="col s12" method="POST" action="${baseUrl}/add" modelAttribute="formModel">
        <form:input path="id" type="hidden"/>

        <div class="row">
            <div class="input-field col s12">
                <form:select path="accountFromId" disabled="${readonly}">
                    <form:options items="${accountChoices}"/>
                </form:select>
                <form:errors path="accountFromId" cssClass="red-text"/>
                <label for="accountFromId">accountFromId</label>
            </div>
        </div>

        <div class="row">
            <div class="input-field col s12">
                <form:select path="accountToId" disabled="${readonly}">
                    <form:options items="${accountChoices}"/>
                </form:select>
                <form:errors path="accountToId" cssClass="red-text"/>
                <label for="accountToId">accountToId</label>
            </div>
        </div>

        <div class="row">
            <div class="input-field col s12">
                <form:input path="amount" type="text" disabled="${readonly}"/>
                <form:errors path="amount" cssClass="red-text"/>
                <label for="amount">amount</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <form:input path="note" type="text" disabled="${readonly}"/>
                <form:errors path="note" cssClass="red-text"/>
                <label for="note">note</label>
            </div>
        </div>

        <div class="row">
            <div class="input-field col s12">
                <form:select path="paymentTypeId" disabled="${readonly}">
                    <form:options items="${paymentTypeChoices}"/>
                </form:select>
                <form:errors path="paymentTypeId" cssClass="red-text"/>
                <label for="paymentTypeId">paymentTypeId</label>
            </div>
        </div>

        <div class="row">
            <div class="input-field col s12">
                <form:select path="transactionType" disabled="${readonly}">
                    <form:options items="${typeChoices}"/>
                </form:select>
                <form:errors path="transactionType" cssClass="red-text"/>
                <label for="transactionType">тип</label>
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
