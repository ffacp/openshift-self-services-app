<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>OpenShift Self Services App</title>

<spring:url value="${contextPath}/css/common.css" var="coreCss" />
<spring:url value="${contextPath}/css/bootstrap.min.css" var="bootstrapCss" />

<link href="${bootstrapCss}" rel="stylesheet" />
<link href="${coreCss}" rel="stylesheet" />

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>

<spring:url value="${contextPath}/js/common.js" var="coreJs" />
<spring:url value="${contextPath}/js/bootstrap.min.js" var="bootstrapJs" />

<script src="${coreJs}"></script>
<script src="${bootstrapJs}"></script>

</head>

<spring:url value="/home" var="urlHome" />
<spring:url value="/projects" var="urlProjects" />
<spring:url value="/quotas" var="urlQuotas" />

<nav class="navbar navbar-inverse ">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="${urlHome}">Home</a>
			<a class="navbar-brand" href="${urlProjects}">Projects</a>
			<a class="navbar-brand" href="${urlQuotas}">Quotas</a>
		</div>
		<div id="navbar">
			<c:if test="${pageContext.request.userPrincipal.name != null}">
				<form id="logoutForm" method="POST" action="/logout">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				</form>
				<ul class="nav navbar-nav navbar-right">
					<li class="active"><a> Welcome ${pageContext.request.userPrincipal.name} </a></li>
					<li class="active"><a class="active" onclick="document.forms['logoutForm'].submit()">Logout</a></li>
				</ul>
			</c:if>
		</div>
	</div>
</nav>