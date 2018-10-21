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

		<form:form method="POST" action="${contextPath}/new-project" modelAttribute="project" class="form-signin">
			<h2 class="form-heading">New Project</h2>
			
			<table class="input-group">
				
				<c:if test="${not empty errors}">
					<tr>
						<td colspan="2">
							<ul class="bg-danger">
								<c:forEach var="e" items="${errors}" >
									<li class="text-danger"> ${e.getField()} ${e.getRejectedValue()}: ${e.getDefaultMessage()} </li>
								</c:forEach>
							</ul>
						</td>
					</tr>
				</c:if>
				
                <tr>
                    <td><form:label path="name" class="control-label" required="required">Name</form:label></td>
                    <td><form:input path="name" class="form-control" autofocus="autofocus" required="required"/></td>
                </tr>
                <tr>
                    <td><form:label path="displayName" class="right-padding">Display Name</form:label></td>
                    <td><form:input path="displayName" class="form-control"/></td>
                </tr>
                <tr>
                    <td><form:label path="description">Description</form:label></td>
                    <td><form:textarea path="description" class="form-control"/></td>
                </tr>
                <tr>
                    <td><form:label path="quotaId">Quota Id</form:label></td>
                    <td><form:input path="quotaId" class="form-control" required="required"/></td>
                </tr>
                <tr>
                    <td><form:label path="quotaOwner">Quota Owner</form:label></td>
                    <td><form:input path="quotaOwner" class="form-control" required="required"/></td>
                </tr>
                
                <tr>
                    <td><input class="btn btn-lg btn-primary" type="submit" value="Create"/></td>
                </tr>
            </table>

		</form:form>

	</div>
	<!-- /container -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
