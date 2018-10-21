<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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

<title>My Projects</title>

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
		<table class="table">
                <tr>
                    <td><h2>My Projects</h2></td>
                    <td>
                    	<h4 class="text-right"><a class="btn btn-lg btn-primary" href="${contextPath}/new-project">Create New Project</a></h4>
                    </td>
                </tr>
            </table>
		
		<table id="projects" class="table">
			<thead>
				<tr class="active">
					<td>Name</td>
					<td>Display Name</td>
					<td>Requester</td>
					<td>Created</td>
					<td>Quota</td>
					<td>Status</td>
					<td></td>
				</tr>
			</thead>

			<tbody>
				<c:if test="${not empty projects}">
					<tr></tr>
				</c:if>

				<c:forEach var="p" items="${projects}" >
					<tr>
						<td>${p.getMetadata().getName()}</td>
						<td>${p.getMetadata().getAnnotations()['openshift.io/display-name']}</td>
						<td>${p.getMetadata().getAnnotations()['openshift.io/requester']}</td>
						<td>${p.getMetadata().getCreationTimestamp()}</td>
						<td>
							${p.getMetadata().getAnnotations()['openshift.io/quota-id']}
							<c:if test="${not empty p.getMetadata().getAnnotations()['openshift.io/quota-owner']}">
								(${p.getMetadata().getAnnotations()['openshift.io/quota-owner']})
							</c:if>
						</td>
						<td>${p.getStatus().getPhase()}</td>
						<td>
							<form id="projectDeleteForm" action="${contextPath}/delete-project" method="POST">
								<input id="projectName" name="projectName" type="hidden" value="${p.getMetadata().getName()}"/>
								<!-- <button class="btn btn-danger btn-block" type="submit" onClick="return confirm('Are you sure?')">Delete</button> -->
								<input type="submit" value="delete" onClick="return confirm('sure?')"/>
							</form>
							<a href="${p.getStatus().getPhase()}">Link</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<!-- /container -->
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
