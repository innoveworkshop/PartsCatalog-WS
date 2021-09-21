<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="href" required="true" %>
<%@ attribute name="classes" required="false" %>
<%@ attribute name="external" required="false" type="java.lang.Boolean" %>
<c:if test="${external == null}"><c:set var="external" value="${false}" /></c:if>

<c:choose>
	<c:when test="${external}">
		<a <c:if test="${classes != null}">class="${classes}"</c:if>
			href="${href}" target="_blank"><jsp:doBody></jsp:doBody></a>
	</c:when>
	<c:otherwise>
		<a <c:if test="${classes != null}">class="${classes}"</c:if>
			href="${pageContext.request.contextPath}${href}"><jsp:doBody></jsp:doBody></a>
	</c:otherwise>
</c:choose>