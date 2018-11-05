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
			<c:when test="${empty quota.createdOn}">
				<h2 class="form-heading">New Quota</h2>
			</c:when>
			<c:otherwise>
				<h2 class="form-heading">Edit Quota "${quota.name}"</h2>
			</c:otherwise>
		</c:choose>
		<br />
		
		<spring:url value="/quotas" var="quotaActionUrl" />
		<form:form class="form-horizontal" method="post" modelAttribute="quota" action="${quotaActionUrl}">
			<fieldset>
				<legend>quota Info:</legend>
				<spring:bind path="name">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<label class="col-sm-2 control-label">Name (Id)</label>
						<div class="col-sm-10">
							<form:input path="name" type="text" class="form-control " id="name" placeholder="Name" autofocus="autofocus" required="required" />
							<form:errors path="name" class="control-label" />
						</div>
					</div>
				</spring:bind>	
				
	            <spring:bind path="owner">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<label class="col-sm-2 control-label">Owner</label>
						<div class="col-sm-10">
							<form:input path="owner" type="text" class="form-control " id="owner" placeholder="owner" required="required" />
							<form:errors path="owner" class="control-label" />
						</div>
					</div>
				</spring:bind>
				
				<spring:bind path="ownerEmail">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<label class="col-sm-2 control-label">Owner Email</label>
						<div class="col-sm-10">
							<form:input path="ownerEmail" type="text" class="form-control " id="ownerEmail" placeholder="ownerEmail" required="required" />
							<form:errors path="ownerEmail" class="control-label" />
						</div>
					</div>
				</spring:bind>
				
				<spring:bind path="cpuLimit">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<label class="col-sm-2 control-label">CPU Limit</label>
						<div class="col-sm-10">
							<form:input path="cpuLimit" type="text" class="form-control " id="cpuLimit" placeholder="cpuLimit" required="required" />
							<form:errors path="cpuLimit" class="control-label" />
						</div>
					</div>
				</spring:bind>
				
				<spring:bind path="cpuRequest">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<label class="col-sm-2 control-label">CPU Request</label>
						<div class="col-sm-10">
							<form:input path="cpuRequest" type="text" class="form-control " id="cpuRequest" placeholder="cpuRequest" required="required" />
							<form:errors path="cpuRequest" class="control-label" />
						</div>
					</div>
				</spring:bind>
				
				<spring:bind path="memoryLimit">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<label class="col-sm-2 control-label">Memory Limit</label>
						<div class="col-sm-10">
							<form:input path="memoryLimit" type="text" class="form-control " id="memoryLimit" placeholder="memoryLimit" required="required" />
							<form:errors path="memoryLimit" class="control-label" />
						</div>
					</div>
				</spring:bind>
				
				<spring:bind path="memoryRequest">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<label class="col-sm-2 control-label">Memory Request</label>
						<div class="col-sm-10">
							<form:input path="memoryRequest" type="text" class="form-control " id="memoryRequest" placeholder="memoryRequest" required="required" />
							<form:errors path="memoryRequest" class="control-label" />
						</div>
					</div>
				</spring:bind>
				
				<spring:bind path="glusterStorage">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<label class="col-sm-2 control-label">Gluster Storage</label>
						<div class="col-sm-10">
							<form:input path="glusterStorage" type="text" class="form-control " id="glusterStorage" placeholder="glusterStorage" required="required" />
							<form:errors path="glusterStorage" class="control-label" />
						</div>
					</div>
				</spring:bind>
				
				<spring:bind path="blockStorage">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<label class="col-sm-2 control-label">Block Storage</label>
						<div class="col-sm-10">
							<form:input path="blockStorage" type="text" class="form-control " id="blockStorage" placeholder="blockStorage" required="required" />
							<form:errors path="blockStorage" class="control-label" />
						</div>
					</div>
				</spring:bind>
				
			</fieldset>
			<spring:url value="/quotas" var="quotasUrl" />
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10" align="right">
					<button type="button" class="btn-lg btn-danger" onclick="location.href='${quotasUrl}'">Cancel</button>
					<c:choose>
						<c:when test="${empty quota.createdOn}">
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