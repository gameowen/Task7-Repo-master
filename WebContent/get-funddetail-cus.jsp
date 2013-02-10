<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="databeans.History" %>
<%@ page import="databeans.Fund" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Mutual Fund Management</title>
<link rel="stylesheet" type="text/css" href="style/main.css">
<link rel="stylesheet" type="text/css" href="style/table.css">
<script type="text/javascript" src="scripts/jquery.js"></script>

<script>
var financeData = [];
var fundName = "No Fund";
<%
ArrayList<History> histories = (ArrayList<History>) request.getAttribute("histories");
Fund fund = (Fund) session.getAttribute("fund");
out.println("financeData = [ ");
if (histories!=null) {
    for (History history : histories) {
        out.println("["+history.getDate().getTime()+","+ history.getPrice() + "],");   
    }
}
out.println("];");
if(fund!=null)
    out.println("fundName = '" + fund.getName() + "';");
%>
</script>

<script type="text/javascript" src="scripts/draw.js"></script>

</head>

<body>
<div id="container">
    <jsp:include page="template-header-navigation.jsp" />
   
        
    <div id="content-container">
        <jsp:include page="template-section-navigation-cus.jsp" />
            
        
	    <div id="content">
		    
	      
	    </div>
    
         <jsp:include page="template-footer.jsp" />
    </div>

<script src="scripts/highstock.js"></script>

</div>

</body>
</html>
