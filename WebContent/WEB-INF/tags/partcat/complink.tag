<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="content" tagdir="/WEB-INF/tags/content" %>
<%@ attribute name="component" required="true" rtexprvalue="true"
	type="com.innoveworkshop.partscatalog.db.models.Component" %>

<content:link href="/component?id=${component.ID}&format=html">
	<jsp:doBody></jsp:doBody>
</content:link>
