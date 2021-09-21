<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="com.innoveworkshop.partscatalog.config.Configuration" %>
<%@ attribute name="href" required="true" %>

<!-- Stylesheets -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/<%= Configuration.CSS_DIR %>${href}">