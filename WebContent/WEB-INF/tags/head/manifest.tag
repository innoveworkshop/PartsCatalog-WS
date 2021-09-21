<%@ tag description="Web manifest" pageEncoding="UTF-8"%>
<%@ tag import="com.innoveworkshop.partscatalog.config.Configuration" %>

<!-- Web Manifest -->
<link rel="manifest" href="${pageContext.request.contextPath}/<%= Configuration.MANIFEST_FILE %>">