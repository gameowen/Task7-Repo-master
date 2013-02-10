<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="databeans.Fund" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Mutual Fund Management</title>
<link rel="stylesheet" type="text/css" href="style/main.css">
</head>
<body>

<div id="container">
    <jsp:include page="template-header-navigation.jsp" />
    <div id="content-container">
        <jsp:include page="template-section-navigation-emp.jsp" />
        
        <div class="content">
            <h2> Transition Day Simulation </h2>
            <jsp:include page="error-list.jsp" />
            <form method="POST" action="transitionday.do">
                
            
                <table>
                    <tr>
                        <td><b style="color: red;">Update Date:</b></td>
                        <td><input type="text" name="date" value="<%=request.getAttribute("date")%>"/></td>
                    </tr>
<%
ArrayList<Fund> funds = (ArrayList<Fund>) request.getAttribute("funds");
if (funds!=null) {
    for (Fund fund : funds) {
%>
                    <tr>
                        <td> <%=fund.getName()%> </td>
                        <td> <input type="text" name="fund_<%=fund.getId()%>" value="<%=fund.getPrice() %>"/> </td>
                    </tr>
<%
    }
}
%>
                    <tr>
                        <td> <input type="submit" name="button" value="Submit"/> </td>
                    </tr>
                </table>
            </form>
        </div>
        <jsp:include page="template-footer.jsp" />
    </div>
</div>
</body>
</html>
        
</body>
</html>