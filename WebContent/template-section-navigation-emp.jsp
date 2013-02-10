<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="databeans.Customer" %>

<div id="section-navigation">
    <ul>
<% 
if (session.getAttribute("employee") == null) {
%>
        <li><span class="menu-item"><a href="employee-login.do">Login</a></span></li>
<%   
} else {
%>
        <li><a href="emp_changePwd.do">Change Password</a></li>
        <li><a href="create-employee-acct.do">Create Employee Account</a></li>
        <li><a href="create-customer-acct.do">Create Customer Account</a></li>
        <li><a href="createfund.do">Create Fund</a></li>
        <li><a href="transitionday.do">Transition Day</a></li>
        <li><a href="manage-customers-emp.jsp">Search Customer</a></li>
        <li><a href="logout-emp.do">Logout</a></li>
        
        <li>&nbsp</li>
        <li>&nbsp</li>
<%
Customer customer = (Customer)session.getAttribute("cus");
	if (customer != null) {
%>
        <li>Actions for <%=customer.getUsername() %></li>
        <li><a href="emp_resetPwd.do">Reset Password</a></li>
        <li><a href="viewcustomeraccount.do">View Account</a></li>
        <li><a href="empviewhistory.do">Transaction History</a></li>
        <li><a href="depositcheck.do">Deposit Check</a></li>        
<%
    }
}
%>
    </ul>
</div>
