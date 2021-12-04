<%--suppress ALL --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="javaTag" uri="http://com.podverbnyj.provider.web" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tables.css" />


<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
      integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
<%--<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>--%>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
<link rel="stylesheet" href="css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/red_stars.css"/>

<%@ include file="success.jspf" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />



<!DOCTYPE html>
<html lang="${language}">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin page for editing and creating users</title>
</head>


<header>
    <c:set var="currentUser" value="${sessionScope.user}"/>


    <c:if test="${currentUser.role.value() != 'admin'}">
        <c:redirect url="index.jsp"/>
    </c:if>

    <div class="page-header">
        <div class="row">
            <div class="col-md-8">
                <h1>Административная страница</h1>

            </div>
            <div class="col-md-2">
                <div class="row align-items-center">
                    <h5>Добро пожаловать</h5><br>
                    <c:if test="${currentUser.name == null}">
                        <h4>
                            <td>${currentUser.login}</td>
                        </h4>
                    </c:if>

                    <c:if test="${currentUser.name != null}">
                        <h4>
                                ${currentUser.name} ${currentUser.surname}
                        </h4>

                    </c:if>

                </div>

            </div>

            <div class="col-md-1">
                <form>
                    <select class="form-select" id="language" name="language" onchange="submit()">
                        <option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
                        <option value="ru" ${language == 'ru' ? 'selected' : ''}>Русский</option>
                    </select>
                </form>

            </div>
            <div class="col-md-1">

                <%@ include file="logout.jspf" %>

            </div>
        </div>
    </div>

    <hr>
    <form action="controller" method="post">


        <input type="hidden" name="command" class="btn btn-primary" value="adminRequest">

        <a href="admin.jsp" class="btn btn-primary" role="button">List of
            services and tariffs</a>

        <button type="submit" name="adminRequest" class="btn btn-success" value="List of users">List of users</button>

        <button type="button" class="btn btn-success" data-bs-toggle="modal"
                data-bs-target="#addOrEditUser">
            Add new user
        </button>


    </form>
    <hr>




</header>
<body>

<div class="form-group">
    <input type="text" class="form-control pull-right" id="search" placeholder="Поиск по таблице">
</div>


<table class="table table-striped table_sort" id="usersTable">
    <caption>Table of users</caption>
    <thead>
    <tr class="table-active">
        <th id = "field 01">Логин</th>
        <th id = "field 02">Общая стоимость услуг</th>
        <th id = "field 03">Email</th>
        <th id = "field 04">Имя</th>
        <th id = "field 05">Фамилия</th>
        <th id = "field 06">Телефон</th>
        <th id = "field 07">Баланс</th>
        <th id = "field 08">Язык</th>
        <th id = "field 09">Роль</th>
        <th id = "field 10">Уведомления</th>
        <th id = "field 11">Статус</th>
        <th id = "field 12"></th>
        <th id = "field 13"></th>
    </tr>
    </thead>

    <c:forEach var="currentUser" items="${ListOfUsers}">
        <tr>
            <td>${currentUser.login}</td>
            <td><javaTag:getTotalCost userID="${currentUser.id}"/></td>
            <td>${currentUser.email}</td>
            <td>${currentUser.name}</td>
            <td>${currentUser.surname}</td>
            <td>${currentUser.phone}</td>
            <td>${currentUser.balance}</td>
            <c:choose>
                <c:when test="${currentUser.language == 'RU'}">
                    <td>Русский</td>
                </c:when>
                <c:otherwise>
                    <td>Английский</td>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${currentUser.role == 'ADMIN'}">
                    <td>Администратор</td>
                </c:when>
                <c:otherwise>
                    <td>Пользователь</td>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${currentUser.notification == 'false'}">
                    <td>Выключено</td>
                </c:when>
                <c:otherwise>
                    <td>Включено</td>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${currentUser.status == 'BLOCKED'}">
                    <td>Заблокирован</td>
                </c:when>
                <c:otherwise>
                    <td>Активен</td>
                </c:otherwise>
            </c:choose>


            <form action="controller" method="post">

                <input type="hidden" name="userToEditId" value="${currentUser.id}">

                <input type="hidden" name="command" class="btn btn-primary" value="adminRequest">
                <td>
                    <button type="submit" name="adminRequest" class="btn btn-secondary btn-sm"
                            value="Add or edit user">Edit user
                    </button>
                </td>
                <td>

                    <c:if test="${currentUser.role == 'ADMIN'}">
                        <button type="submit" name="adminRequest" class="btn btn-danger btn-sm"
                                value="Delete user">Delete user
                        </button>
                    </c:if>
                    <c:if test="${currentUser.role == 'USER'}">
                        <c:if test="${currentUser.status == 'BLOCKED'}">
                            <button type="submit" name="adminRequest" class="btn btn-danger btn-sm"
                                    value="Unblock user">Unblock user
                            </button>
                        </c:if>
                        <c:if test="${currentUser.status == 'ACTIVE'}">
                            <button type="submit" name="adminRequest" class="btn btn-danger btn-sm"
                                    value="Block user">Block user
                            </button>
                        </c:if>
                    </c:if>

                </td>

            </form>

        </tr>

    </c:forEach>
</table>

<script>

    $("#search").keyup(function() {
        var value = this.value;

        $("table").find("tr").each(function(index) {
            if (!index) return;
            var id = $(this).find("td").text();
            $(this).toggle(id.indexOf(value) !== -1);
        });
    });


</script>

<%@ include file="sorter_table.jspf" %>




<!-- Модальное окно -->
<div class="modal fade" id="deleteSUserConfirmation" tabindex="-1" aria-labelledby="deleteSUserConfirmation"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Delete confirmation window</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                Are you sure you want to delete user account?<br>
                Data can't be restored!!!
            </div>

            <div class="modal-footer">
                <form action="controller" method="post">
                    <input type="hidden" name="command" value="adminRequest">
                    <input type="hidden" name="confirmation" value="true">
                    <button type="submit" class="btn btn-danger" name="adminRequest" value="Delete user">Delete
                    </button>
                </form>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        var hash = window.location.hash;
        if (hash == '#deleteSUserConfirmation') {
            $("#deleteSUserConfirmation").modal('show');
            history.pushState("", document.title, window.location.pathname
                + window.location.search);
        }
    })
</script>

<div class="modal fade" id="lastAdminDeleteError" tabindex="-1" aria-labelledby="lastAdminDeleteError"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Last admin user can't be deleted</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>


<script>
    $(document).ready(function () {
        var hash = window.location.hash;
        if (hash == '#lastAdminDeleteError') {
            $("#lastAdminDeleteError").modal('show');
            history.pushState("", document.title, window.location.pathname
                + window.location.search);
        }
    })
</script>

<%@ include file="add_or_edit_user.jspf" %>


</body>
</html>
