<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
      integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<link rel="stylesheet" href="css">
<%@ include file="success.jspf" %>



<!DOCTYPE html>
<html>
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
            <div class="col-md-3">
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
                    <input type="button" value="Logout" class="btn btn-primary" onClick='location.href="index.jsp"'>
                </form>
            </div>
        </div>
    </div>

</header>
<body>

<hr>
<form action="controller" method="post">


    <input type="hidden" name="command" class="btn btn-primary" value="adminRequest">

    <a href="admin.jsp" class="btn btn-primary" role="button">List of
        services and tariffs</a>

    <%--    <a class="btn btn-primary" href="admin.jsp#editOrDeleteService" onClick="window.location.reload();" role="button">Add new service</a>--%>
    <%--    <input type="submit" name="adminRequest" class="btn btn-primary" value="Add new tariff"/>--%>
    <button type="submit" name="adminRequest" class="btn btn-success" value="List of users">List of users</button>

    <button type="button" class="btn btn-success" data-bs-toggle="modal"
            data-bs-target="#addOrEditUser">
        Add new user
    </button>


</form>
<hr>
<form action="controller" method="post">
    <input type="hidden" name="command" value="sort">
    <button type="submit" name="sort" class="btn btn-secondary btn-sm" value="Sort users by login">Sort users by login
    </button>


</form>
<hr>

<table class="table table-bordered">
    <thead>
    <tr class="table-active">
        <th>Логин</th>
        <th>Пароль</th>
        <th>Email</th>
        <th>Имя</th>
        <th>Фамилия</th>
        <th>Телефон</th>
        <th>Баланс</th>
        <th>Язык</th>
        <th>Роль</th>
        <th>Уведомления</th>
        <th>Статус</th>
        <th></th>
        <th></th>
    </tr>
    </thead>

    <c:forEach var="user" items="${ListOfUsers}">
        <tr>
            <td>${user.login}</td>
            <td>*****</td>
            <td>${user.email}</td>
            <td>${user.name}</td>
            <td>${user.surname}</td>
            <td>${user.phone}</td>
            <td>${user.balance}</td>
            <c:choose>
                <c:when test="${user.language == 'RU'}">
                    <td>Русский</td>
                </c:when>
                <c:otherwise>
                    <td>Английский</td>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${user.role == 'ADMIN'}">
                    <td>Администратор</td>
                </c:when>
                <c:otherwise>
                    <td>Пользователь</td>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${user.notification == 'false'}">
                    <td>Выключено</td>
                </c:when>
                <c:otherwise>
                    <td>Включено</td>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${user.status == 'BLOCKED'}">
                    <td>Заблокирован</td>
                </c:when>
                <c:otherwise>
                    <td>Активен</td>
                </c:otherwise>
            </c:choose>


            <form action="controller" method="post">

                <input type="hidden" name="userToEditId" value="${user.id}">

                <input type="hidden" name="command" class="btn btn-primary" value="adminRequest">
                <td>
                    <button type="submit" name="adminRequest" class="btn btn-secondary btn-sm"
                            value="Add or edit user">Edit user
                    </button>
                </td>
                <td>

                    <c:if test="${user.role == 'ADMIN'}">
                        <button type="submit" name="adminRequest" class="btn btn-danger btn-sm"
                                value="Delete user">Delete user
                        </button>
                    </c:if>
                    <c:if test="${user.role == 'USER'}">
                        <c:if test="${user.status == 'BLOCKED'}">
                            <button type="submit" name="adminRequest" class="btn btn-danger btn-sm"
                                    value="Unblock user">Unblock user
                            </button>
                        </c:if>
                        <c:if test="${user.status == 'ACTIVE'}">
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