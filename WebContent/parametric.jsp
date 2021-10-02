<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.innoveworkshop.partscatalog.config.Configuration" %>
<%@ taglib prefix="content" tagdir="/WEB-INF/tags/content" %>
<%@ taglib prefix="partcat" tagdir="/WEB-INF/tags/partcat" %>

<content:page>
	<!-- Parametric Search -->
	<table id="parametric" border="0" cellpadding="3" cellspacing="0">
		<thead>
			<tr>
				<th>Categories</th>
				<th>Sub-Categories</th>
				<th>Packages</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td align="center">
					<select multiple name="categories" size="10"
							id="list-categories">
						<c:forEach var="category" items="${categories}">
							<option value="${category.ID}">${category.name}</option>
						</c:forEach>
					</select>
				</td>
				<td align="center">
					<select multiple name="subcategories" size="10"
							id="list-subcategories">
						<c:forEach var="subcategory" items="${subcategories}">
							<option value="${subcategory.ID}"
									class="parent-${subcategory.parentCategory.ID}">
								${subcategory.name}
							</option>
						</c:forEach>
					</select>
				</td>
				<td align="center">
					<select multiple name="packages" size="10"
							id="list-packages">
						<c:forEach var="casestyle" items="${casestyles}">
							<option value="${casestyle.ID}">${casestyle.name}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td align="center">
					<button onclick="resetList('list-categories')"
						type="reset">Reset</button>
				</td>
				<td align="center">
					<button onclick="resetList('list-subcategories')"
						type="reset">Reset</button>
				</td>
				<td align="center">
					<button onclick="resetList('list-packages')"
						type="reset">Reset</button>
				</td>
			</tr>
		</tbody>
	</table>
	<br>
	<button onclick="resetLists([ 'list-categories', 'list-subcategories', 'list-packages' ])"
		type="reset">Reset All</button>
	<button type="submit">Apply Filters</button>
	
	<hr>
	<br>
	
	<!-- Results Table -->
	<table id="results" border="1" cellpadding="2" cellspacing="1">
		<thead>
			<tr>
				<th>Compare</th>
				<th>
					<content:image src="/icons/datasheet.png"
							classes="datasheet-image" border="0">
						Datasheet
					</content:image>
				</th>
				<th>Image</th>
				<th>Part Number</th>
				<th>Description</th>
				<th>Quantity</th>
				<th>Category</th>
				<th>Sub-Category</th>
				<th>Package</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="component" items="${object.collection}">
				<partcat:paramrow component="${component}"></partcat:paramrow>
			</c:forEach>
		</tbody>
	</table>
	<br>
	<button type="submit">Compare Selected</button>
</content:page>	
