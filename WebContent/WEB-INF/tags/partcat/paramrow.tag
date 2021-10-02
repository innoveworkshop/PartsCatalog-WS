<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="content" tagdir="/WEB-INF/tags/content" %>
<%@ taglib prefix="partcat" tagdir="/WEB-INF/tags/partcat" %>
<%@ attribute name="component" required="true" rtexprvalue="true"
	type="com.innoveworkshop.partscatalog.db.models.Component" %>

<tr id="row-${component.ID}">
	<td align="center"><input type="checkbox"></td>
	<td align="center">
		<!--datasheet-->
			<a href="/datasheet/datasheet.pdf">
				<content:image src="/icons/datasheet.png"
						classes="datasheet-image" border="0">
					Datasheet
				</content:image>
			</a>
		<!--endif-->
	</td>
	<td align="center">
		<partcat:compimg component="${component}"></partcat:compimg>
	</td>
	<td align="center">
		<partcat:complink component="${component}">${component.name}</partcat:complink>
	</td>
	<td>${component.description}</td>
	<td align="center">${component.quantity}</td>
	<td align="center">
		<a href="/category/${component.category.ID}">
			${component.category.name}
		</a>
	</td>
	<td align="center">
		<c:if test="${component.subCategory != null}">
			<a href="/subcategory/${component.subCategory.ID}">
				${component.subCategory.name}
			</a>
		</c:if>
	</td>
	<td align="center">
		<c:if test="${component.caseStyle != null}">
			<a href="/package/${component.caseStyle.ID}">
				${component.caseStyle.name}
			</a>
		</c:if>
	</td>
</tr>