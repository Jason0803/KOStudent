<%@page import="javax.naming.NamingException"%>
<%@page import="java.sql.Connection"%>
<%@page import="javax.sql.DataSource"%>
<%@ page import="javax.naming.InitialContext"%>
<%@ page import="javax.naming.Context" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2 align="center">Connection Pooling Test</h2>
	<% 
		/*
			1. Naming Service Instance	
		*/
		try {
		Context ic = new InitialContext();
		DataSource ds = (DataSource)ic.lookup("java:comp/env/jdbc/oracleDB");
		
		out.println("<b>DataSource...Lookup...</b><p>");
		Connection conn = ds.getConnection();
		out.println("<h2>Connection Rent Success !</h2><p>");
		} catch(NamingException e) {
			out.println("<h2>Connection Rent Fail !</h2><p>");
		}
	%>
</body>
</html>