<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title> Mutual Fund Management </title>
    <link rel="stylesheet" type="text/css" href="style/main.css" />
    <link rel="stylesheet" type="text/css" href="style/table.css" />
    <script type="text/javascript" src="scripts/submit.js"></script>
</head>

<body>
    <div id="container">
    <jsp:include page="template-header-navigation.jsp" />
    <div id="content-container">
        <jsp:include page="template-section-navigation-emp.jsp" />
            <div class="content">
                <h2>Search for Customers</h2>
                <jsp:include page="error-list.jsp" />
                <form action=""> 
				<input class="searchinput" type="text" name="customer" placeholder="Type in username OR first name OR last name to search"
				    onkeyup="showCustomer(this.value)" onchange="showCustomer(this.value)"/>
				</form>
				<br/>
				<table class="tableone">
                    <thead>
                        <tr>
                            <th class="th3"> Username </th>
                            <th class="th3"> First Name </th>
                            <th class="th3"> Last Name </th> 
                            <th class="th3"> City </th>
                            <th class="th3"> State </th>          
                        </tr>
                    </thead>
                    <tfoot>
                    <tr><td colspan="5">Click ROW to view details for each account.</td></tr>
                    </tfoot>
                    
                    <tbody>
	                    <tr><td colspan="5">
	                    <div class="innerb">
	                    <table class="tabletwo" id="txtHint">
	                    
	                    </table>
	                    </div>
	                    </td></tr>
                    </tbody>
                
                </table>
            </div>
            
            <jsp:include page="template-footer.jsp" />
            
        </div>
    </div>
</body>
</html>
