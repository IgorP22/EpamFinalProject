<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
      integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>


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

<!-- Start of Table -->
<table class="table table-bordered">
    <thead>
    <tr class="table-active">
        <th>Наименование пакета</th>
        <th>Описание</th>
        <th>Цена</th>
    </tr>
    </thead>
    <tbody>

<c:forEach var="service" items="${ListOfServices}">
    <tr class="table-primary" style="font-size: 1.2em">
        <td>${service.titleRu}</td>
        <td></td>
        <td></td>
    </tr>

    <c:forEach var="tariff" items="${ListOfTariffs}">
        <c:if test="${service.id == tariff.serviceId}">
            <tr>
            <td>${tariff.nameRu}</td>
            <td>${tariff.descriptionRu}</td>
            <td>${tariff.price}</td>
            </tr>
        </c:if>
    </c:forEach>
</c:forEach>

    </tbody>
</table>




<!--<a href="hello-servlet">Hello Servlet</a> -->
<hr>
<form action="controller" method="post">
    <input type="hidden" name="command" value="sort">
    <input type="submit" name="sort" value="Sort services"/>
    <input type="submit" name="sort" value="Sort tariffs by name"/>
    <input type="submit" name="sort" value="Sort tariffs by price"/>


</form>
<hr>

<form action="controller" method="post">
    <input type="hidden" name="command" value="email">
    <input type="submit" name="Email me" value="Send price list by email"/>
</form>

<form action="controller" method="post">
    <input type="hidden" name="command" value="download">
    <input type="submit" name="download" value="Download prise list"/>

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
