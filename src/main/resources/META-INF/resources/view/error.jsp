<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">

<jsp:include page="fragments/header.jsp" />

<body>

	<div class="container">

		<h1>Error Page</h1>

		<p>${exception}</p>
		<%-- <c:if test="${not empty exception[message]}">
			<p>Exception: ${exception.message}</p>
		</c:if>
		<c:if test="${not empty exception[stackTrace]}">
		  	<c:forEach items="${exception.stackTrace}" var="stackTrace"> 
				${stackTrace} 
			</c:forEach>
	  	</c:if> --%>

	</div>

	<jsp:include page="fragments/footer.jsp" />

</body>
</html>