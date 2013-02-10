<%@page import="databeans.Customer"%>
<%@page import="databeans.Fund"%>
<%@ page import="java.util.*" %>
<%@ page import="utility.Format" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
            <h2>Buy Fund </h2>
            <jsp:include page="error-list.jsp" />
            <p>
                Type in the dollar amount if you want to buy the specified fund. 
                Remember, it should be number.
            </p>
<%
Customer customer = (Customer) session.getAttribute("customer");
if(customer == null) {
    out.print("There is no customer information found in session!");
    return;
}
%>
<%
Fund fund = (Fund) session.getAttribute("fund");
if(fund == null){
    out.print("You need to select a fund first");
    return;
}
%>
            <p> 
                Your Cash Balance: <%=Format.formatMoney(customer.getAvailableCash())%>
            </p>
            <form method="post" action="cus_buyFund.do">
                <table>
                    <tr>
                        <td><b> Fund Name: </b></td>
                        <td><a href="#" onclick="javascript:submitFund('<%=fund.getId() %>')"> <%=fund.getName() %> </a></td>
                    </tr>
                    <tr>
                        <td><b> Amount you want to buy:</b> $ </td>
                        <td><input type="text" name="amount" value="0.00"/></td>
                        <td>No less than 1 dollar</td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center">
                            <input type="submit" name="button" value="Submit"/>
                        </td>
                    </tr>
                </table>
                <input type="hidden" name="fundId" value="<%=fund.getId()%>"/>
            </form>
        </div>
        <jsp:include page="template-footer.jsp" />
    </div>
</div>
</body>
</html>
