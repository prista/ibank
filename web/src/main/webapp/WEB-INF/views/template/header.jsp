<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="contextUrl" value="${pageContext.request.contextPath}"/>

<header>
	<nav>
		<div class="nav-wrapper container">
			<ul id="dropdown1" class="dropdown-content">
				<li><a href="#!">one</a></li>
				<li><a href="#!">two</a></li>
				<li class="divider"></li>
				<li><a href="#!">three</a></li>
			</ul>
			<ul id="nav-mobile" class="left hide-on-med-and-down">
                <li><a href="${contextUrl}/">Home</a></li>
				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<li><a href="${contextUrl}/bank">Bank</a></li>
				</sec:authorize>
				<sec:authorize access="!isAnonymous()">
					<li><a href="${contextUrl}/branch">Branch</a></li>
				</sec:authorize>
                <li><a href="${contextUrl}/userprofile">UserProfile</a></li>
                <li><a href="${contextUrl}/account">Account</a></li>
				<li><a href="${contextUrl}/transaction">Transaction</a></li>
                <li><a href="${contextUrl}/payment-type">Payment Type</a></li>
				<li><a href="${contextUrl}/ajax-samples">AJAX</a></li>
				<li><a class="dropdown-trigger" href="#!" data-target="dropdown1">Dropdown<i
						class="material-icons right">arrow_drop_down</i></a></li>
				<sec:authorize access="!isAnonymous()">
					<a class="right" href="${contextUrl}/execute_logout" title="logout"><i class="material-icons">arrow_forward</i></a>
				</sec:authorize>
				<sec:authorize access="isAnonymous()">
					<li><a href="${contextUrl}/login">Sign in</a></li>
				</sec:authorize>
				<sec:authentication property="principal"/>
			</ul>
		</div>
	</nav>
</header>