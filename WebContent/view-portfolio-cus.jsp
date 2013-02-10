<%@page import="utility.Format"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="databeans.Portfolio"%>
<%@page import="databeans.Customer"%>
<%@page import="databeans.Transaction"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Mutual Fund Management</title>
<link rel="stylesheet" type="text/css" href="style/main.css">
<script type="text/javascript" src="scripts/submit.js"></script>
</head>
<body>

	<div id="container">
		<jsp:include page="template-header-navigation.jsp" />
		<div id="content-container">

			<jsp:include page="template-section-navigation-cus.jsp" />

			<div class="content">
				<h2>Customer Account</h2>
				<jsp:include page="error-list.jsp" />

<%
Customer customer = (Customer) session.getAttribute("customer");
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
						<td><%=customer.getAddrL1()%><%=customer.getAddrL2()%></td>
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

				<p>&nbsp</p>

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
						<td><a href="#" onclick="javascript:sellfund('<%=portfolio.getFundId()%>');">Sell</a></td>
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
			<jsp:include page="template-footer.jsp" />
		</div>
	</div>
</body>
</html>