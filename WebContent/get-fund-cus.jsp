<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="databeans.Fund" %>
<%@ page import="java.text.DecimalFormat" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style/table.css"/>

</head>
<body>

<%
ArrayList<Fund> funds = (ArrayList<Fund>) request.getAttribute("funds");
SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
DecimalFormat nf = new DecimalFormat("#,##0.00");
nf.setMaximumFractionDigits(2);
nf.setMinimumFractionDigits(2);
DecimalFormat nf2 = new DecimalFormat("#,##0.00");
nf2.setMaximumFractionDigits(3);
nf2.setMinimumFractionDigits(3);
if (funds!=null) {
	for (Fund fund : funds) {
%>
	    <tr onclick="javascript:submitFund('<%=fund.getId() %>')">
		    <td class="td1"> <%=fund.getId() %> </td>
		    <td class="td2"> <%=fund.getName() %> </td>
		    <td class="td3"> <%=fund.getSymbol() %> </td>
		    <td class="td4"> <%=format.format(fund.getDate()) %> </td>
		    <td class="td5"><font face="courier"> $<%=nf.format(fund.getPrice()) %></font></td>
	    
	    </tr>
<%
	}
}
%>

</body>
</html>