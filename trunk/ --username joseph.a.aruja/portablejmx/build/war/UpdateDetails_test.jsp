<%@page language="java"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="org.leedsmet.studentresearch.util.Constants"%>
<%@page import="org.leedsmet.studentresearch.util.Constants.Misc"%>
<jsp:useBean id="validUser" class="org.leedsmet.studentresearch.jmx.valueobjects.UserVO" scope="session" />

<html lang="en">
    <head>
		<meta http-equiv="Content-Language" content="en-gb">
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
		<META NAME="Language" CONTENT="English">
		<META NAME="Author" CONTENT="Joseph Aruja">
		<META NAME="Keywords" CONTENT="jmxportability">
		<META NAME="Description" CONTENT="This is for testing JMX beans per session">
		<title>
			JMX portability Test
		</title>
		<link rel=stylesheet href="css/jmx.css" type="text/css">
	</head>
<body>
<BR>
<BR>

<br>
<br>
<table align ="left">
<thead>MyDetails</thead>
<tr>
<td>Name : </td>
<td><%=validUser.getUserName()%></td>
</tr>
<tr>
<td>Logged in at</td>
<td><%=validUser.getLogInTime()%></td>
</tr>

</table>


<form name="LogOutForm" method="post" action="portability.jmx">
<input type="hidden" name="action" value="<%=Constants.UserActions.LOGOUT%>" >
<input type="submit" name="LogOutAction" value="LogOut"/>
</form>


</body>
</html>


