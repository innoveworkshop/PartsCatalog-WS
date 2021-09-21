<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag import="com.innoveworkshop.jsp.utils.TitleMaker" %>
<%@ taglib prefix="head" tagdir="/WEB-INF/tags/head" %>
<%@ taglib prefix="content" tagdir="/WEB-INF/tags/content" %>
<%@ attribute name="title" required="false" %>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title><%= TitleMaker.getTitle(title) %></title>
	
	<head:mobiledesc></head:mobiledesc>
	<head:favicons></head:favicons>
	<head:manifest></head:manifest>
	<head:stylesheet href="/default.css"></head:stylesheet>
	<head:script href="/parametric.js"></head:script>
</head>
<body>
	<!-- Header -->
	<div id="header">
		<a href="/">
			<h1>PartsCatalog</h1>
		</a>
		
		<div class="menu">
			<a href="/">Home</a>
			<a href="/projects">Projects</a>
		</div>
	</div>
	
	<!-- Main Content Area -->
	<div id="main">
		<jsp:doBody></jsp:doBody>
	</div>
	
	<content:footer></content:footer>
</body>
</html>