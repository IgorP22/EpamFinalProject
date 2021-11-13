<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html>
<head>
    <title>Welcome to provider main page</title>
</head>
<body>
<h1><%= "Добро пожаловать" %>
</h1>
<br/>

<h2><%= "Наша улуги и цены" %>
</h2>
<br/>


<!-- ${ListOfServices} -->
<!-- ${ListOfTariffs} -->



<c:forEach var="service" items="${ListOfServices}">
    <h4>${service.titleRu}</h4>

    <!-- <c:set var="service_id" value="${service.id}"/> -->

    <c:forEach var="tariff" items="${ListOfTariffs}">

      <!--  <c:set var="tariff_id" value="${tariff.serviceId}"/> -->



        <c:if test="${service.id == tariff.serviceId}">
            ${tariff.nameRu}
            ${tariff.price}<br>
        </c:if>
    </c:forEach>
</c:forEach>
<!--<a href="hello-servlet">Hello Servlet</a> -->
<hr>
<form action="controller" method="post">
    <input type="hidden" name="command" value="sort">
    <input type="submit" name="sort" value="Sort services" />
    <input type="submit" name="sort" value="Sort tariffs by name" />
    <input type="submit" name="sort" value="Sort tariffs by price" />


</form>
<hr>

<form action="controller" method="post">

    <input type="submit" name="Email me" value="Send price list by email" />
    <input type="submit" name="Download" value="Download price list" />


</form>

<hr>


<form action="controller" method="post">
    <input type="hidden" name="command" value="login">
    <input name="login"><br>
    <input type="password" name="password"><br>
    <input type="submit" value="Login">
</form>

</body>
</html>
