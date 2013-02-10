<%@ page import="java.util.*" %>

<%
ArrayList<String> errors = (ArrayList<String>) request.getAttribute("errors");

if(errors!=null) {
    for (String error : errors) {
        out.println("<div style=\"font-size:medium; color:red; \"> " + error + "</div>");
    }
}

%>