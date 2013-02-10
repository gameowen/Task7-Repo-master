<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="databeans.Customer" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="style/table.css"/>
</head>
<body>


<%
ArrayList<Customer> customers = (ArrayList<Customer>) request.getAttribute("customers");
if(customers!=null) {
    for (Customer customer : customers) {
	    out.println("<tr onclick=\" javascript:submitCustomer('"+ customer.getUsername() +"')  \">");
	    out.println("  <td class=\"td4\">"+customer.getUsername()+ "</td>");
	    out.println("  <td class=\"td4\">"+customer.getFirstName() + "</td>");
	    out.println("  <td class=\"td4\">"+customer.getLastName() + "</td>");
	    out.println("  <td class=\"td4\">"+customer.getCity()+ "</td>");
	    out.println("  <td class=\"td4\">"+customer.getState() + "</td>");
	    out.println("</tr>");
	    out.println("</form>");
    }
}
%>

</body>
</html>