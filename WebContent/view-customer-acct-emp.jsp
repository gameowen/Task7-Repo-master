<%@page import="databeans.Portfolio"%>
<%@page import="databeans.Customer"%>
<%@page import="databeans.Transaction"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.Date"%>
<%@ page import="utility.Format"%>
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

			<div class="content">
				<h2>Customer Account</h2>
<%
Customer customer = (Customer) session.getAttribute("cus");
if (customer != null) {
    Date date = (Date)request.getAttribute("lastExecuteDate");
%>

				<table border="1">
					<tr>
						<td><b>Name</b></td>
						<td><%=customer.getFirstName()%> <%=customer.getLastName()%></td>
					</tr>
					<tr>
						<td><b>Address</b></td>
						<td><%=customer.getAddrL1()%> <%=customer.getAddrL2()%></td>
					</tr>
					<tr>
						<td><b>City</b></td>
						<td><%=customer.getCity()%></td>
					</tr>
					<tr>
						<td><b>State</b></td>
						<td><%=customer.getState()%></td>
					</tr>
					<tr>
						<td><b>Zip</b></td>
						<td><%=customer.getZip()%></td>
					</tr>
					<tr>
					<tr>
                        <td><b>Last Trading Date</b></td>
                        <td><%=date==null?"N/A":Format.formatDate(date)%></td>
                    </tr>
					<tr>
                        <td><b>Cash Balance</b></td>
                        <td align="right"><font face="courier"><%=Format.formatMoney(customer.getCash())%></font></td>
                    </tr>
                    <tr>
                        <td><b>Available Balance</b></td>
                        <td align="right"><font face="courier"><%=Format.formatMoney(customer.getAvailableCash())%></font></td>
                    </tr>
				</table>
				<h2></h2>
				<b>Funds Owned By <%=customer.getFirstName()%> <%=customer.getLastName()%> Currently</b>				
				<table border="1">
					<tr>
						<td><b>Fund Name</b></td>
						<td><b>Share Amount</b></td>
						<td><b>Value per Share</b></td>
						<td><b>Total Value</b></td>
					</tr>
					<%
						ArrayList<Portfolio> portfolios = (ArrayList<Portfolio>) request.getAttribute("portfolios");
						if (portfolios != null) {
							for (Portfolio portfolio : portfolios) {
					%>
					<tr>
						<td><%=portfolio.getFundName()%></td>
						<td align="right"><font face="courier"><%=Format.formatShares(portfolio.getShares())%></font></td>
                        <td align="right"><font face="courier"><%=Format.formatMoney(portfolio.getPrice())%></font></td>
                        <td align="right"><font face="courier"><%=Format.formatMoney(portfolio.getTotal())%></font></td>
					</tr>
					<%
						}
						}
					%>
				</table>
			</div>
<%
}
%>
			<div id="footer">Copyright © Mutual Fund Application by Team
				e-Motion | CMU MSIT ebusiness Task7 2013</div>
		</div>
	</div>
</body>
</html>