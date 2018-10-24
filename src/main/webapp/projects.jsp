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
		<c:if test="${not empty msg}">
			<div class="alert alert-${css} alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<strong>${msg}</strong>
			</div>
		</c:if>
		
		<table class="table">
			<tr>
			    <td><h2>My Projects</h2></td>
			    <td>
			    	<h4 class="text-right"><a class="btn btn-lg btn-primary" href="${contextPath}/projects/add">Create New Project</a></h4>
			    </td>
			</tr>
		</table>
		
		<table id="projects" class="table">
			<thead>
				<tr class="active">
					<td>Name</td>
					<td>Display Name</td>
					<td>Requester</td>
					<td>Created On</td>
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
						<td>${p.name}</td>
						<td>${p.displayName}</td>
						<td>${p.requester}</td>
						<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${p.createdOn}" /></td>
						<td>
							${p.quotaId}
							<c:if test="${not empty p.quotaOwner}">
								(${p.quotaOwner})
							</c:if>
						</td>
						<td>${p.status}</td>
						<td>
							<spring:url value="/projects/${p.name}" var="viewUrl" />
							<spring:url value="/projects/${p.name}/delete?${_csrf.parameterName}=${_csrf.token}" var="deleteUrl" /> 
							<spring:url value="/projects/${p.name}/update" var="updateUrl" />
							<spring:url value="${p.url}" var="ocpUrl" />
							
							<!-- <button class="btn btn-info" onclick="location.href='${userUrl}'">Query</button>
							<button class="btn btn-primary" onclick="location.href='${updateUrl}'">Update</button> -->
							
							<c:if test="${not empty p.requester}">
								<button class="btn btn-danger" onclick="this.disabled=true;post('${deleteUrl}')">Delete</button>
							</c:if>
							
							<%-- <button class="btn btn-info" onclick="location.href='${ocpUrl}'">Go To</button> --%>
							<a href="${p.url}" target="_blank" class="btn btn-info">Link</a>
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
	
	<jsp:include page="fragments/footer.jsp" />
</body>
</html>
