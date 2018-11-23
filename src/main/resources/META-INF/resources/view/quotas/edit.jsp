<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="en">

<jsp:include page="../fragments/header.jsp" />
<script type="text/javascript">
$(document).ready(function(){
	
	showHideAdancedFeilds();
	
	$("#showAdvanced").change(showHideAdancedFeilds);	
    $("#cpuLimit").change(calcCPURequest);
    $("#memoryLimit").change(calcMemoryRequest);
});

function calcCPURequest(){
	var cpuLimit = $("#cpuLimit").val();
	var cpuRequest = Math.ceil(cpuLimit * .2);
	$("#cpuRequest").val(cpuRequest);
}

function calcMemoryRequest(){
	var memoryLimit = $("#memoryLimit").val();
	var memoryRequest = Math.ceil(memoryLimit * .8);
	$("#memoryRequest").val(memoryRequest);
}

function showHideAdancedFeilds(){
	var showAdvanced = $('#showAdvanced').is(':checked');
	if(showAdvanced == true){
		$(".advanced").show();
	}else{
		$(".advanced").hide();
	}
}
</script>
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
			<c:when test="${empty quota.uid}">
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
				
				<legend>Quota Info:</legend>
				<spring:bind path="name">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<label class="col-sm-2 control-label">Quota Id</label>
						<div class="col-sm-10" style="width: 400px">
							<form:input path="name" type="text" class="form-control " id="name" placeholder="o-ad-dev-1" autofocus="autofocus" required="required" 
								readonly="${!empty quota.uid}" />
							<form:errors path="name" class="control-label" />
						</div>
					</div>
				</spring:bind>
				
				<spring:bind path="uid">
					<form:hidden path="uid" class="form-control " id="uid" />
				</spring:bind>
				
	            <spring:bind path="owner">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<label class="col-sm-2 control-label">Owner</label>
						<div class="col-sm-10" style="width: 400px">
							<form:input path="owner" type="text" class="form-control " id="owner" placeholder="username" required="required" />
							<form:errors path="owner" class="control-label" />
						</div>
					</div>
				</spring:bind>
				
				<spring:bind path="ownerEmail">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<label class="col-sm-2 control-label">Owner Email</label>
						<div class="col-sm-10" style="width: 400px">
							<form:input path="ownerEmail" type="text" class="form-control " id="ownerEmail" placeholder="Owner Email" required="required" />
							<form:errors path="ownerEmail" class="control-label" />
						</div>
					</div>
				</spring:bind>
				
				<legend>Compute:</legend>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">Show Advanced</label>
					<div class="col-sm-10" style="width: 400px">
						<input type="checkbox" name="showAdvanced" id="showAdvanced" /> 
					</div>
				</div>
				
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<spring:bind path="cpuLimit.amount">
						<label class="col-sm-2 control-label">CPU Limit (Cores)</label>
						<div class="col-sm-10" style="width: 400px">
							<form:input path="cpuLimit.amount" type="number" class="form-control " id="cpuLimit" placeholder="10" required="required" />
							<form:errors path="cpuLimit.amount" class="control-label" />
						</div>					
					</spring:bind>
				
					<spring:bind path="cpuRequest.amount">
						<label class="col-sm-2 control-label advanced">CPU Request (Cores)</label>
						<div class="col-sm-10 advanced" style="width: 400px">
							<form:input path="cpuRequest.amount" type="number" class="form-control " id="cpuRequest" placeholder="2" required="required" />
							<form:errors path="cpuRequest.amount" class="control-label" />
						</div>
					</spring:bind>
				</div>
				
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<spring:bind path="memoryLimit.amount">					
						<label class="col-sm-2 control-label">Memory Limit (Gi)</label>
						<div class="col-sm-10" style="width: 400px">
							<form:input path="memoryLimit.amount" type="number" class="form-control " id="memoryLimit" placeholder="10" required="required" />
							<form:errors path="memoryLimit.amount" class="control-label" />
						</div>
					</spring:bind>
				
					<spring:bind path="memoryRequest.amount">
						<label class="col-sm-2 control-label advanced">Memory Request (Gi)</label>
						<div class="col-sm-10 advanced" style="width: 400px">
							<form:input path="memoryRequest.amount" type="number" class="form-control " id="memoryRequest" placeholder="8" required="required" />
							<form:errors path="memoryRequest.amount" class="control-label" />
						</div>
					</spring:bind>
				</div>
				
				<legend>Storage:</legend>
				<spring:bind path="glusterStorage.amount">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<label class="col-sm-2 control-label">Gluster Storage (Gi)</label>
						<div class="col-sm-10" style="width: 400px">
							<form:input path="glusterStorage.amount" type="number" class="form-control " id="glusterStorage" placeholder="50" required="required" />
							<form:errors path="glusterStorage.amount" class="control-label" />
						</div>
					</div>
				</spring:bind>
				
				<spring:bind path="blockStorage.amount">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<label class="col-sm-2 control-label advanced">Block Storage (Gi)</label>
						<div class="col-sm-10 advanced" style="width: 400px">
							<form:input path="blockStorage.amount" type="number" class="form-control " id="blockStorage" placeholder="0" required="required" />
							<form:errors path="blockStorage.amount" class="control-label" />
						</div>
					</div>
				</spring:bind>
				
			</fieldset>
			<spring:url value="/quotas" var="quotasUrl" />
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10" style="width: 400px" align="right">
					<button type="button" class="btn-lg btn-danger" onclick="location.href='${quotasUrl}'">Cancel</button>
					<c:choose>
						<c:when test="${empty quota.uid}">
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