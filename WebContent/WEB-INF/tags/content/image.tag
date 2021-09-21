<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag import="com.innoveworkshop.partscatalog.config.Configuration" %>
<%@ attribute name="src" required="true" %>
<%@ attribute name="classes" required="false" %>
<%@ attribute name="align" required="false" %>
<%@ attribute name="style" required="false" %>
<%@ attribute name="external" required="false" type="java.lang.Boolean" %>
<c:if test="${external == null}"><c:set var="external" value="${false}" /></c:if>

<img <c:if test="${classes != null}">class="${classes}"</c:if>
	src="<c:if test="${!external}">${pageContext.request.contextPath}/<%= Configuration.IMAGE_DIR %></c:if>${src}"
	alt="<jsp:doBody></jsp:doBody>"
	<c:if test="${align != null}">align="${align}"</c:if>
	<c:if test="${style != null}">style="${style}"</c:if>>