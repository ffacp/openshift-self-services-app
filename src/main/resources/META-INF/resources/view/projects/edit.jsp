<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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
		
		<c:choose>
			<c:when test="${empty project.createdOn}">
				<h2 class="form-heading">New Project</h2>
			</c:when>
			<c:otherwise>
				<h2 class="form-heading">Edit Project "${project.name}"</h2>
			</c:otherwise>
		</c:choose>
		<br />
		
		<spring:url value="/projects" var="projectActionUrl" />
		<form:form class="form-horizontal" method="post" modelAttribute="project" action="${projectActionUrl}">
			<fieldset>
				<legend>Project Info:</legend>
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
			</fieldset>
			<spring:url value="/projects" var="projectsUrl" />
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10" align="right">
					<button type="button" class="btn-lg btn-danger" onclick="location.href='${projectsUrl}'">Cancel</button>
					<c:choose>
						<c:when test="${empty project.createdOn}">
							<button type="submit" class="btn-lg btn-primary">Add</button>
						</c:when>
						<c:otherwise>
							<button type="submit" class="btn-lg btn-primary">Update</button>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</form:form>
	
	</div>
	
	<jsp:include page="../fragments/footer.jsp" />

</body>
</html>