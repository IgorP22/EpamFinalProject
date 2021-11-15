<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
      integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
<link rel="stylesheet" href="css/boo">


<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to provider main page</title>
</head>
<body>

<header>
    <c:set var="user" value="${sessionScope.user}"/>

    <c:if test="${user.role.value() != 'admin'}">
        <c:redirect url = "index.jsp"/>
    </c:if>

    <div class="page-header">
        <div class="row">
            <div class="col-md-8">
                <h1>Административная страница</h1>

            </div>
            <div class="col-md-3">
                <div class="row align-items-center">
                    <h5>Добро пожаловать</h5><br>
                    <c:if test="${user.name == null}">
                        <h4><td>${user.login}</td></h4>
                    </c:if>

                    <c:if test="${user.name != null}">
                        <h4><td>${user.name}</td></h4>
                        <h4><td>" "</td></h4>
                        <h4><td>${user.surname}</td></h4>
                    </c:if>

                </div>

            </div>
            <div class="col-md-1">
                <form>
                    <input type="button" value="Logout" class="btn btn-primary" onClick='location.href="index.jsp"'>
                </form>
            </div>
        </div>
    </div>

</header>
<body>
<%--<hr>--%>
<%--<form action="controller" method="post">--%>
<%--<div class="dropdown">--%>
<%--    <button class="btn btn-primary dropdown-toggle" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">--%>
<%--       Получить списки--%>
<%--    </button>--%>
<%--    <ul class="dropdown-menu">--%>

<%--    <input type="hidden" name="command" value="getList">--%>
<%--        <li><input type="submit" name="getList" value="List of services and tariffs"/></li>--%>
<%--        <li><input type="submit" name="getList" value="Add new service"/></li>--%>
<%--        <li><input type="submit" name="getList" value="Add new tariff"/></li>--%>
<%--        <li><input type="submit" name="getList" value="List of users"/></li>--%>
<%--        <li><input type="submit" name="getList" value="Add new user"/></li>--%>
<%--    </ul>--%>

<%--</div>--%>
<%--</form>--%>
<%--<hr>--%>

<hr>
<form action="controller" method="post">


            <input type="hidden" name="command" class="btn btn-primary" value="adminRequest">
            <input type="submit" name="adminRequest" class="btn btn-primary" value="List of services and tariffs"/>
            <input type="submit" name="adminRequest" class="btn btn-primary" value="Add new service"/>
            <input type="submit" name="adminRequest" class="btn btn-primary" value="Add new tariff"/>
            <input type="submit" name="adminRequest" class="btn btn-success" value="List of users"/>
            <input type="submit" name="adminRequest" class="btn btn-success" value="Add new user"/>
</form>
<hr>

<c:set var="adminFlag" value="${sessionScope.adminFlag}"/>

<c:if test="${adminFlag == 'price'}">

    <hr>
    <form action="controller" method="post">
        <input type="hidden" name="command" value="sort">
        <input type="submit" name="sort" class="btn btn-secondary btn-sm" value="Sort services"/>
        <input type="submit" name="sort" class="btn btn-secondary btn-sm" value="Sort tariffs by name"/>
        <input type="submit" name="sort" class="btn btn-secondary btn-sm" value="Sort tariffs by price"/>

    </form>
    <hr>

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
</c:if>



</body>




