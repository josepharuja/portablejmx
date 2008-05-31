<%@page language="java"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="org.leedsmet.studentresearch.util.Constants"%>
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

<div class="header">
	<div id="logo" style="text-align:right;"><img src="images/java.jpg" alt="NHS Logo" LONGDESC=""></div>
</div>
<br>
<br>
<h1>Convenient way of creating MBeans in a Web applications</h1>
<br>
<br>
<p>
This is to demonstrate how MBeans are created for an application/usersession.The application level MBeans can be used for configuration/administration purposes.
These user level MBeans can be used to track the user activity.
</p>

<BR>
<BR>
<table>
<tr>
<td font="arial" color="red">
<%=request.getParameter(Constants.Misc.USER_MSG)!=null?request.getParameter(Constants.Misc.USER_MSG):""%>
</td>
</tr>
</table>

<form name="LoginForm" method="post" action="portability.jmx">
<table align="left">
<tr>
<td>
Enter your Name
</td>
<td>
<input type="text" name="userName"/>
</td>
</tr>
<tr>
<td>
Enter your password
</td>
<td>
<input type="password" name="userPassword"/>
</td>
</tr>
<tr>
<td>
<input type="hidden" name="action" value="<%=org.leedsmet.studentresearch.util.Constants.UserActions.AUTHENTICATE%>" >
<input type="submit" name="LoginAction" value="Login"/>
</td>
</tr>
</table>
</form>
</body>
</html>
