<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style/table.css"/>

</head>
<body>

<%
    Connection con;
    ResultSet rs = null;
    String url = "jdbc:mysql://localhost:3306/task7";
    
    String query = (String) request.getParameter("fund");
    Class.forName("com.mysql.jdbc.Driver").newInstance();
    con = DriverManager.getConnection(url, "", ""); 
    
    PreparedStatement pstmt = con.prepareStatement("SELECT * FROM task7_fund " +
            " WHERE fund_id REGEXP ? " +
            " OR symbol REGEXP ? " +
            " OR name REGEXP ?");
    /*
            pstmt = con.prepareStatement("select task7_fund.fund_id, task7_fund.name, " +
            " task7_fund.symbol, task7_history.price_date, task7_history.price " +
            " from task7_fund inner join task7_history " +
            " on task7_fund.fund_id = task7_history.fund_id " +
            " where task7_fund.fund_id REGEXP ? " +
            " OR task7_fund.symbol REGEXP ? " +
            " OR task7_fund.name REGEXP ?");
    */
    pstmt.setString(1,query);
    pstmt.setString(2,query);
    pstmt.setString(3,query);
    
    rs = pstmt.executeQuery();
/*    
    out.println("<table class='tableone' >");
    out.println("<thead>");
    out.println("   <tr>");
    out.println("       <th class=\"th1\"> ID </th>");
    out.println("       <th class=\"th2\"> Fund Name </th>");
    out.println("       <th class=\"th3\"> Fund Symbol </th>");
    out.println("       <th class=\"th4\"> Date </th>");
    out.println("       <th class=\"th5\"> Price </th>");
    out.println("   </tr>");
    out.println("</thead>");
   
    pstmt = con.prepareStatement("select max(price_date) from task7_history");
    ResultSet rss = pstmt.executeQuery();
    
    if (rss.next()){
        out.println("<tfoot>");
        out.println("   <tr>");
        out.println("   <td colspan=\"5\"> LAST UPDATE DATE: " + rss.getDate("max(price_date)") + "</td>");
        out.println("   </tr>");
        out.println("</tfoot>");
    }
    
    out.println("<tbody>");
    out.println("<tr><td colspan=\"5\">");
    out.println("<div class=\"innerb\">");
    out.println("   <table class=\"tabletwo\">");
*/
    while (rs.next()) {        
        String fundId = rs.getString("fund_id");
        pstmt = con.prepareStatement("select max(price_date), price from task7_history where fund_id = ?");
        pstmt.setString(1, fundId);
        ResultSet rs2 = pstmt.executeQuery();
        
        out.println("<tr onclick=\" javascript:submitFund("+ fundId +")  \">");
        out.println("<td class=\"td1\">"+rs.getString("fund_id") + "</td>");
        out.println("<td class=\"td2\">"+rs.getString("name") + "</td>");
        out.println("<td class=\"td3\">"+rs.getString("symbol") + "</td>");
        if(rs2.next()){
            out.println("<td class=\"td4\">"+rs2.getDate("max(price_date)") + "</td>");
            out.println("<td class=\"td5\">"+rs2.getString("price") + "</td>");
        } else {
            out.println("<td class=\"td4\">"+ "" + "</td>");
            out.println("<td class=\"td5\">"+ "" + "</td>");
        }
        out.println("</tr>");
    }
/*
    out.println("   </table>");
    out.println("</div>");
    out.println("</td></tr>");
    
    out.println("</tbody>");
*/    
    
    rs.close();
    
    con.close();


%>

</body>
</html>