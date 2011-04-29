<%-- 
    Document   : response
    Created on : Oct 29, 2010, 11:32:20 AM
    Author     : uko
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
		<jsp:useBean id="mybean" scope="session" class="org.mypackage.hello.Worker" />
		<div><jsp:getProperty name="mybean" property="data" /></div>
    </body>
</html>
