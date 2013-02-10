<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="section-navigation">
    <ul>
<% 
if (session.getAttribute("customer") == null) {
%>
        <li><span class="menu-item"><a href="customer-login.do">Login</a></span></li>
<%   
} else {
    if(session.getAttribute("fund") != null) {
%>
        <li><span class="menu-item"><a href="cus_buyFund.do">Buy Fund</a></span></li>
        <li>&nbsp</li>
        <li>&nbsp</li>
<%
    }
%>
        <li><span class="menu-item"><a href="search-fund-cus.jsp">Research Fund</a></span></li>
		<li><span class="menu-item"><a href="requestcheck.do">Request Check</a></span></li>
		<li><span class="menu-item"><a href="cusviewhistory.do">Transaction History</a></span></li>
		<li><span class="menu-item"><a href="viewportfolio.do">View Account</a></span></li>
        <li><span class="menu-item"><a href="cus_changePwd.do">Change password</a></span></li>
		<li><span class="menu-item"><a href="logout-cus.do">Logout</a></span></li>
<%
}
%>
    </ul>
</div>