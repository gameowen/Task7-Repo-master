<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create Customer Account</title>
<link rel="stylesheet" type="text/css" href="style/main.css">
</head>
<body>

<div id="container">
    <jsp:include page="template-header-navigation.jsp" />
    <div id="content-container">
        <jsp:include page="template-section-navigation-emp.jsp" />
        
        <div class="content">
            <h2> Create Customer Account </h2>
            
            <jsp:include page="error-list.jsp" />
            
			<form method="post" action="create-customer-acct.do">
				<table>
					<tr>
						<td> User Name: </td>
						<td><input type="text" name="userName" value="<%=((request.getParameter("userName")==null)?"":request.getParameter("userName"))%>"/></td>
					</tr>
					<tr>
						<td> First Name: </td>
						<td><input type="text" name="firstName" value="<%=((request.getParameter("firstName")==null)?"":request.getParameter("firstName"))%>"/></td>
					</tr>
					<tr>
						<td> LastName: </td>
						<td><input type="text" name="lastName" value="<%=((request.getParameter("lastName")==null)?"":request.getParameter("lastName"))%>"/></td>
					</tr>
					<tr>
						<td> Password: </td>
						<td><input type="password" name="password" value=""/></td>
						<td> (Please enter numbers or letters)</td>
					</tr>
					<tr>
						<td> Confirm Password: </td>
						<td><input type="password" name="confirmPassword" value=""/></td>
					</tr>
					<tr>
		 				<td> Address Line1: </td>
						<td><input type="text" name="addr1" value="<%=((request.getParameter("addr1")==null)?"":request.getParameter("addr1"))%>"/></td>
					</tr>
					<tr>
						<td> Address Line2: </td>
						<td><input type="text" name="addr2" value="<%=((request.getParameter("addr2")==null)?"":request.getParameter("addr2"))%>"/></td>
					</tr>
					<tr>
						<td> City: </td>
						<td><input type="text" name="city" value="<%=((request.getParameter("city")==null)?"":request.getParameter("city"))%>"/></td>
					</tr>
					<tr>
						<td> State: </td>
						<td><input type="text" name="state" value="<%=((request.getParameter("state")==null)?"":request.getParameter("state"))%>"/></td>
					</tr>
					<tr>
						<td> Zip: </td>
						<td><input type="text" name="zip" value="<%=((request.getParameter("zip")==null)?"":request.getParameter("zip"))%>"/></td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<input type="submit" name="button" value="Create Account"/>
						</td>
					</tr>
				</table>
			</form>
        </div>
        <jsp:include page="template-footer.jsp" />
    </div>
</div>
</body>
</html>
