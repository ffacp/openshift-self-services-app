<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">

<jsp:include page="../fragments/header.jsp" />

<body>
	
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
			    <td><h2>My Cluster Quotas</h2></td>
			    <td>
			    	<security:authorize access="hasAuthority('cluster-admin')">
			    		<h4 class="text-right"><a class="btn btn-lg btn-primary" href="${contextPath}/quotas/add">Create New Quota</a></h4>
			    	</security:authorize>
			    </td>
			</tr>
		</table>
		
		<table id="projects" class="table">
			<thead>
				<tr class="active" style="font-weight: bold;">
					<td rowspan="2" style="vertical-align: middle;">Name(Id)</td>
					<td rowspan="2" style="vertical-align: middle;">Owner</td>
					<td rowspan="2" style="vertical-align: middle;">Created On</td>
					<td rowspan="2" style="vertical-align: middle;">Expires On</td>
					<td colspan="2" align="center">CPU</td>
					<td colspan="2" align="center">Memory</td>
					<td colspan="2" align="center">Storage</td>
					<td rowspan="2"></td>
				</tr>
				<tr class="active" style="font-weight: bold;">
					<td>Request</td>
					<td>Limit</td>
					<td>Request</td>
					<td>Limit</td>
					<td>Gluster</td>
					<td>Block</td>
				</tr>
			</thead>

			<tbody>
				<c:if test="${not empty quotas}">
					<tr></tr>
				</c:if>

				<c:forEach var="q" items="${quotas}" >
					<tr>
						<td>${q.name}</td>
						<td>
							${q.owner}
							<%-- <c:if test="${not empty q.ownerEmail}">
								(${q.ownerEmail})
							</c:if> --%>
						</td>
						<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${q.createdOn}" /></td>
						<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${q.expireOn}" /></td>
						
						<td>${q.cpuRequest}</td>
						<td>${q.cpuLimit}</td>
						<td>${q.memoryRequest}</td>
						<td>${q.memoryLimit}</td>
						<td>${q.glusterStorage}</td>
						<td>${q.blockStorage}</td>
						
						<td align="right">
							<spring:url value="/quotas/${q.name}" var="viewUrl" />
							<spring:url value="/quotas/${q.name}/delete?${_csrf.parameterName}=${_csrf.token}" var="deleteUrl" /> 
							<spring:url value="/quotas/${q.name}/update" var="updateUrl" />
							<%-- <spring:url value="${p.url}" var="ocpUrl" /> --%>
							
							<button class="btn btn-info" onclick="location.href='${viewUrl}'">View</button>
							
							
							<security:authorize access="hasAuthority('cluster-admin')">
								<button class="btn btn-primary" onclick="location.href='${updateUrl}'">Edit</button>
								<button class="btn btn-danger" onclick="this.disabled=true;post('${deleteUrl}')">Delete</button>
							</security:authorize>
							
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
	<jsp:include page="../fragments/footer.jsp" />
</body>
</html>