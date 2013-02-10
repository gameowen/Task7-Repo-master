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
            <jsp:include page="template-section-navigation-cus.jsp" />

            <div class="content">
				<h2>Change Password</h2>

				<jsp:include page="error-list.jsp" />
				
				<form method="post" action="cus_changePwd.do">
					<table>
						<tr>
							<td>New Password:</td>
							<td><input type="password" name="newPassword" value="" /></td>
							<td> (Please enter numbers or letters)</td>
						</tr>
						<tr>
							<td>Confirm New Password:</td>
							<td><input type="password" name="confirmPassword" value="" /></td>
						</tr>
						<tr>
							<td colspan="2" align="center"><input type="submit"
								name="button" value="Submit" /></td>
						</tr>
					</table>
				</form>
				<jsp:include page="template-footer.jsp" />
			</div>
		</div>
	</div>
</body>
</html>
