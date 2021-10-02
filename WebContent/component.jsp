<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.innoveworkshop.partscatalog.config.Configuration" %>
<%@ taglib prefix="content" tagdir="/WEB-INF/tags/content" %>
<%@ taglib prefix="partcat" tagdir="/WEB-INF/tags/partcat" %>
<c:set var="component" scope="session" value="${object}"/>

<content:page>
	<!-- Component Summary -->
	<table id="summary-container" cellspacing="1" border="0">
		<tbody>
			<tr>
				<!-- Summary -->
				<td valign="top">
					<table id="summary-table" border="1" cellspacing="1" cellpadding="2">
						<tbody>
							<tr>
								<th>Manufacturer Part Number</th>
								<td>${component.name}</td>
							</tr>
							<tr>
								<th>Quantity Available</th>
								<td>${component.quantity}</td>
							</tr>
							<tr>
								<th>Category</th>
								<td>
									<a href="/category/${component.category.ID}">
										${component.category.name}
									</a>
								</td>
							</tr>
							<tr>
								<th>Sub-Category</th>
								<td>
									<c:if test="${component.subCategory != null}">
										<a href="/subcategory/${component.subCategory.ID}">
											${component.subCategory.name}
										</a>
									</c:if>
								</td>
							</tr>
							<tr>
								<th>Description</th>
								<td>${component.description}</td>
							</tr>
							<!-- if has Datasheet -->
								<tr>
									<th>Datasheet</th>
									<td>
										<a href="/datasheet/datasheet.pdf">
											${component.name}
										</a>
									</td>
								</tr>
							<!-- endif -->
						</tbody>
					</table>
					
					<!-- Quantity Editor -->
					<br>
					<table id="quantity-editor">
						<thead>
							<tr>
								<th>Quantity</th>
								<th>Part Number</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>
									<input type="text" size="9" maxlength="9" value="">
								</td>
								<td>${component.name}</td>
								<td>
									<button type="submit">Add to Stock</button>
								</td>
								<td>
									<button type="submit">Remove from Stock</button>
								</td>
							</tr>
						</tbody>
					</table>
					<br>
				</td>
	
				<!-- Component Image -->
				<td valign="top">
					<div class="image-container">
						<partcat:compimg component="${component}"></partcat:compimg>
						<br>
						<span>Image shown is a representation only. Exact
							specifications should be obtained from the product data
							sheet.</span>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
	
	<!-- Component Properties -->
	<table id="properties" border="1" cellspacing="1" cellpadding="2">
		<tbody>
			<!-- if has Datasheet -->
				<tr>
					<th>Datasheet</th>
					<td>
						<a href="/datasheet/datasheet.pdf">
							${component.name}
						</a>
					</td>
				</tr>
			<!-- endif -->
			<tr>
				<th>Category</th>
				<td>
					<a href="/category/${component.category.ID}">
						${component.category.name}
					</a>
				</td>
			</tr>
			<tr>
				<th>Family</th>
				<td>
					<c:if test="${component.subCategory != null}">
						<a href="/subcategory/${component.subCategory.ID}">
							${component.subCategory.name}
						</a>
					</c:if>
				</td>
			</tr>
			<c:forEach var="property" items="${component.properties}">
				<tr>
					<th>${property.name}</th>
					<td>${property.value}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</content:page>
