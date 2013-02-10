<%@page import="databeans.Portfolio"%>
<%@page import="databeans.Customer"%>
<%@page import="databeans.Transaction"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.Date"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Customer template</title>
<link rel="stylesheet" type="text/css" href="style/main.css">
</head>
<body>

	<div id="container">
		<div id="header">
			<h1>Carnegie Financial Service - Mutual Fund Management</h1>
		</div>
		<div id="navigation">
			<ul>
				<li><a href="index.html">Home</a></li>
				<li><a href="#">About</a></li>
				<li><a href="#">Services</a></li>
				<li><a href="#">Contact us</a></li>
			</ul>
		</div>
		<div id="content-container">

			<jsp:include page="template-section-navigation-emp.jsp" />
							<%
					DecimalFormat nf = new DecimalFormat("$#0.00");
					nf.setMaximumFractionDigits(2);
					nf.setMinimumFractionDigits(2);
				%>

			<div class="content">
				<h2>Congratulations</h2>
				<h3>Fund has been created</h3>	

			<div id="footer">Copyright © Mutual Fund Application by Team
				e-Motion | CMU MSIT ebusiness Task7 2013</div>
		</div>
	</div>
</body>
</html>