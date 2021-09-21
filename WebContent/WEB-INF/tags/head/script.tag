<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag import="com.innoveworkshop.partscatalog.config.Configuration" %>
<%@ attribute name="href" required="true" %>
<%@ attribute name="defer" required="false" type="java.lang.Boolean" %>
<c:if test="${defer == null}"><c:set var="defer" value="${true}" /></c:if>

<!-- Javascript -->
<c:choose>
	<c:when test="${defer}">
		<script defer type="text/javascript"
			src="${pageContext.request.contextPath}/<%= Configuration.JS_DIR %>${href}"></script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/<%= Configuration.JS_DIR %>${href}"></script>
	</c:otherwise>
</c:choose>