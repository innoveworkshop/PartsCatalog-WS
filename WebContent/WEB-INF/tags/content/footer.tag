<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="com.innoveworkshop.partscatalog.config.Configuration" %>
<%@ tag import="java.time.LocalDate" %>
<%@ taglib prefix="content" tagdir="/WEB-INF/tags/content" %>

<!-- Footer -->
<div id="footer">
	<p align="center">
		Copyright © 2021-<%= LocalDate.now().getYear() %>
		<a href="<%= Configuration.COMPANY_URL %>"><%= Configuration.COMPANY_NAME %></a>
	</p>
</div>