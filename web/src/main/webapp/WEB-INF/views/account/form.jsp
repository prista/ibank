<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h4 class="header">Edit account</h4>

<c:set var="baseUrl" value="${pageContext.request.contextPath}/account"/>

<div class="row">
    <form:form class="col s12" method="POST" action="${baseUrl}/add" modelAttribute="formModel">
        <form:input path="id" type="hidden"/>
        <div class="row">
            <div class="input-field col s12">
                <form:input path="name" type="text" disabled="${readonly}"/>
                <form:errors path="name" cssClass="red-text"/>
                <label for="name">name</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <form:select path="userProfileId" disabled="${readonly}">
                    <form:options items="${userProfileChoices}"/>
                </form:select>
                <form:errors path="userProfileId" cssClass="red-text"/>
                <label for="userProfileId">userProfile</label>
            </div>
        </div>

        <div class="row">
            <div class="input-field col s12">
                <form:select path="accountType" disabled="${readonly}">
                    <form:options items="${typeChoices}"/>
                </form:select>
                <form:errors path="accountType" cssClass="red-text"/>
                <label for="accountType">тип</label>
            </div>
        </div>

        <div class="row">
            <div class="input-field col s12">
                <form:input path="balance" type="text" disabled="true"/>
                <form:errors path="balance" cssClass="red-text"/>
                <label for="balance">balance</label>
            </div>
        </div>

        <div class="row">
            <div class="input-field col s12">
                <form:select path="currency" disabled="${readonly}">
                    <form:options items="${currenciesChoices}"/>
                </form:select>
                <form:errors path="currency" cssClass="red-text"/>
                <label for="currency">currency</label>
            </div>
        </div>

        <div class="row">
            <div class="input-field col s12">
                <form:select path="bankId" disabled="${readonly}">
                    <form:options items="${bankChoices}"/>
                </form:select>
                <form:errors path="bankId" cssClass="red-text"/>
                <label for="bankId">bank</label>
            </div>
        </div>

        <div class="row">
            <div class="input-field col s12">
                <div class="switch">
                    <label> unlocked <form:checkbox path="locked" disabled="${readonly}"/> <span class="lever"></span>
                        locked
                    </label>
                </div>
                <label class="switch-label">is locked</label> <br/>
            </div>
        </div>

        <div class="row">
            <div class="input-field col s12">
                <div class="switch">
                    <label> exist <form:checkbox path="deleted" disabled="${readonly}"/> <span class="lever"></span>
                        deleted
                    </label>
                </div>
                <label class="switch-label">is deleted</label> <br/>
            </div>
        </div>

        <div class="row">
            <div class="input-field  col s12">
                <form:textarea path="card.cardType" disabled="${readonly}"/>
                <form:errors path="card.cardType" cssClass="red-text"/>
                <label for="card.cardType">Card Type</label>
            </div>
        </div>

        <div class="row">
            <div class="input-field col s12">
                <form:input path="card.expirationDate" type="text" disabled="${readonly}" cssClass="datepicker"/>
                <form:errors path="card.expirationDate" cssClass="red-text"/>
                <label for="card.expirationDate">Exp date</label>
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
