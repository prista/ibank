<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h4 class="header">AJAX samples</h4>


<div style="margin: 10px;">
    <form:form class="col s12" method="POST" action="${pageContext.request.contextPath}/ajax-samples"
               modelAttribute="addressForm">
        <div class="row">
            <div class="col s12">
                <form:select path="country" cssClass="browser-default"/>
            </div>
        </div>
        <div class="row">
            <div class="col s12">
                <form:select path="region" cssClass="browser-default"/>
            </div>
        </div>
        <div class="row">
            <div class="col s12">
                <form:select path="city" cssClass="browser-default"/>
            </div>
        </div>
        <input type="submit"/>
    </form:form>
</div>

<script src="${pageContext.request.contextPath}/resources/js/init-combos.js"></script>
<script>
    initComboboxes('${pageContext.request.contextPath}');
</script>

