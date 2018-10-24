<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<meta name="description" content="">
<meta name="author" content="">

<title>Create New Project</title>

<link href="${contextPath}/resources/css/bootstrap.min.css"
	rel="stylesheet">
</head>
<body>
	<div class="container">

		<c:if test="${pageContext.request.userPrincipal.name != null}">
			<form id="logoutForm" method="POST" action="${contextPath}/logout">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
			</form>

			<h4 align="right">
				Welcome ${pageContext.request.userPrincipal.name} | <a
					onclick="document.forms['logoutForm'].submit()">Logout</a>
			</h4>

		</c:if>

	</div>
	
	<div class="container">
		<h2 class="form-heading">New Project</h2>
		<spring:url value="/projects" var="projectActionUrl" />
		<form:form class="form-horizontal" method="post" modelAttribute="project" action="${projectActionUrl}">
				
				<%-- <c:if test="${not empty errors}">
					<tr>
						<td colspan="2">
							<ul class="bg-danger">
								<c:forEach var="e" items="${errors}" >
									<li class="text-danger"> ${e.getField()} ${e.getRejectedValue()}: ${e.getDefaultMessage()} </li>
								</c:forEach>
							</ul>
						</td>
					</tr>
				</c:if> --%>
			
			<spring:bind path="name">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Name</label>
					<div class="col-sm-10">
						<form:input path="name" type="text" class="form-control " id="name" placeholder="Name" autofocus="autofocus" required="required" />
						<form:errors path="name" class="control-label" />
					</div>
				</div>
			</spring:bind>	
			
            <spring:bind path="displayName">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Display Name</label>
					<div class="col-sm-10">
						<form:input path="displayName" type="text" class="form-control " id="displayName" placeholder="displayName" />
						<form:errors path="displayName" class="control-label" />
					</div>
				</div>
			</spring:bind>
			
			<spring:bind path="description">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Description</label>
					<div class="col-sm-10">
						<form:textarea path="description" type="text" class="form-control " id="description" placeholder="description" />
						<form:errors path="description" class="control-label" />
					</div>
				</div>
			</spring:bind>
			
			<spring:bind path="quotaId">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Quota Id</label>
					<div class="col-sm-10">
						<form:input path="quotaId" type="text" class="form-control " id="quotaId" placeholder="quotaId" required="required" />
						<form:errors path="quotaId" class="control-label" />
					</div>
				</div>
			</spring:bind>
			
			<spring:bind path="quotaOwner">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Quota Owner</label>
					<div class="col-sm-10">
						<form:input path="quotaOwner" type="text" class="form-control " id="quotaOwner" placeholder="quotaOwner" required="required" />
						<form:errors path="quotaOwner" class="control-label" />
					</div>
				</div>
			</spring:bind>
			
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<%-- <c:choose>
						<c:when test="${project['new']}"> --%>
							<button type="submit" class="btn-lg btn-primary pull-right">Add</button>
						<%-- </c:when>
						<c:otherwise>
							<button type="submit" class="btn-lg btn-primary pull-right">Update</button>
						</c:otherwise>
					</c:choose> --%>
				</div>
			</div>
		</form:form>

	</div>
	<!-- /container -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
