<%@page import="java.util.List"%>
<%@page import="databeans.Fund"%>
<%@page import="databeans.Position"%>
<%@ page import="java.util.*" %>
<%@ page import="utility.Format" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sell Fund</title>
<link rel="stylesheet" type="text/css" href="style/main.css">
<script type="text/javascript" src="scripts/submit.js"></script>
</head>
<body>

<div id="container">
    <jsp:include page="template-header-navigation.jsp" />
    	<div id="content-container">
        	<jsp:include page="template-section-navigation-cus.jsp" />
       			 <div class="content">
            		<h2> Sell Fund </h2>
            
            <jsp:include page="error-list.jsp" />
            
            <p>Type in the number of shares you want to sell. Remember, it could not be larger than the number you have.</p>
            <hr />
            <form method="post" action="cus_sellFund.do">
                <table>
                    <tr>
                        <td> <div align="center">FundName:  </div></td>
                        <td> <div align="center">Shares you own now </div></td>
                        <td> <div align="center">Shares you will sell</div></td>
                    </tr>
                    <tr>
					<%
					Fund fund = (Fund) request.getAttribute("fund");
					if(fund!=null){
						
					%>
					<td><a href="#" onclick="javascript:submitFund('<%=fund.getId() %>')"> <%=fund.getName() %></a></td>
					<td><%=Format.formatShares(fund.getShares()) %></td>
					<td><input type="text" name="shares" value="0"/></td>
					<td><input type="hidden" name="fundId" value="<%=fund.getId()%>"/></td>
					<%
						
					}
					%>
                    <tr>
                        <td colspan="3" align="center"><input type="submit" name="button" value="submit"/></td>
                    </tr>
                </table>
            </form>
        </div>
        <jsp:include page="template-footer.jsp" />
    </div>
</div>
</body>
</html>
