<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="content" tagdir="/WEB-INF/tags/content" %>
<%@ taglib prefix="partcat" tagdir="/WEB-INF/tags/partcat" %>
<%@ attribute name="component" required="true" rtexprvalue="true"
	type="com.innoveworkshop.partscatalog.db.models.Component" %>

<c:if test="${component.image != null}">
	<partcat:complink component="${component}">
		<content:image classes="comp-image" border="0" external="true"
				src="${pageContext.request.contextPath}/image?format=jpeg&id=${component.image.ID}&download=true">
			Image
		</content:image>
	</partcat:complink>
</c:if>