<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome to provider main page</title>
</head>
<body>
<h1><%= "Welcome to provider main page" %>
</h1>
<br/>


<form action="controller" method="post">
    <input type="hidden" name="command" value="login">
    <input name="login"><br>
    <input type="password" name="password"><br>
    <input type="submit" value="Login">
</form>



<a href="hello-servlet">Hello Servlet</a>
</body>
</html>