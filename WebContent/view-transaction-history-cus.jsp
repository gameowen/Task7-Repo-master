<%@page import="databeans.Transaction"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<%@page import="utility.Format"%>
<%@page import="java.text.DecimalFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Customer template</title>
<link rel="stylesheet" type="text/css" href="style/main.css">
</head>
<body>
	<div id="container">
		<jsp:include page="template-header-navigation.jsp" />
		<div id="content-container">
			<jsp:include page="template-section-navigation-cus.jsp" />

			<div class="content">
				<h2>Transaction History</h2>

				<jsp:include page="error-list.jsp" />
				<form method="post" action="history.do">
					<table width="auto" border="1">
						<tr>
							<th width="auto">Date</th>
							<th width="auto">Operation</th>
							<th width="auto">Fund Name</th>
							<th width="auto">Share</th>
							<th width="auto">Price</th>
							<th width="auto">Dollar Amount</th>
                            <th width="auto">Status</th>
                        </tr>
						
<%
ArrayList<Transaction> transactions = (ArrayList<Transaction>)request.getAttribute("transactions");
if(transactions!=null) {
    for(Transaction transaction : transactions){
        boolean buy = transaction.getTransaction_type().equalsIgnoreCase("BUY");
        boolean sell = transaction.getTransaction_type().equalsIgnoreCase("SELL");
        boolean processed = transaction.getStatus().equalsIgnoreCase("APPROVED") 
                || transaction.getStatus().equalsIgnoreCase("DENIED");
%>
			            <tr>
							<td><div align="center"> <%=transaction.getExecute_dateStr() %></div></td>
							<td><div align="center"> <%=transaction.getTransaction_type() %></div></td>
							<td><div align="center"> <%=buy||sell?transaction.getFundName():"N/A" %></div></td>
							<td><div align="right"><font face="courier"><%=!((buy&&!processed)||(!buy&&!sell))?Format.formatShares(transaction.getShares()):"N/A" %></font></div></td>
							<td><div align="right"><font face="courier"><%=processed&&(buy||sell)?Format.formatMoney(transaction.getFundPrice()):"N/A" %></font></div></td>
	   						<td><div align="right"><font face="courier"><%=!(!processed && sell)?Format.formatMoney(transaction.getAmount()):"N/A" %></font></div></td>
			                <td><div align="center"><%=transaction.getStatus() %></div></td>
                        </tr>
<%
    }
}
%>	
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
