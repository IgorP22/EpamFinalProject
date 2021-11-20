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


<div class="modal fade" id="addOrEditUser" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
     aria-labelledby="addOrEditUser" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Данные пользователя</h5>

            </div>
            <form action="controller" method="post">

                <div class="modal-body">
                    <c:set var="userToEdit" value="${userToEdit}"/>


                    <c:if test="${userToEdit != null}">
                    <input type="hidden" name="userToEditId" value="${userToEdit.id}">
                    </c:if>


                    <div class="mb-3">
                        <label for="userLogin" class="col-form-label">Логин:</label>
                        <input type="text" name="userLogin" class="form-control" id="userLogin"
                               value="${userToEdit.login}"
                               minlength="5" maxlength="30" placeholder="Допустимы любые символы" required>

                        <div class="row">
<%--                            <c:if test="${userToEdit.password == null}">--%>
<%--                                <c:set var="holder" value="Допустимы любые символы"/>--%>
<%--                            </c:if>--%>
<%--                            <c:if test="${userToEdit.password != null}">--%>
<%--                                <c:set var="holder" value="*****"/>--%>
<%--                            </c:if>--%>
                            <div class="form-group col-md-6">
                                <label for="userPassword" class="col-form-label">Пароль:</label>
                                <input type="password" name="userPassword" class="form-control" id="userPassword"
                                       value="${userToEdit.password}" onkeyup='passwordValidation();'
                                       minlength="5" maxlength="150" placeholder="Допустимы любые символы" required>
                                <span style="font-size: smaller" id='message'></span>
                            </div>

                            <div class="form-group col-md-6">
                                <label for="confirmPassword" class="col-form-label">Повторите ввод пароля:</label>
                                <input type="password" name="confirmPassword" class="form-control"
                                       id="confirmPassword" onkeyup='passwordValidation();'
                                       value="${userToEdit.password}"
                                       minlength="5" maxlength="30" placeholder="Допустимы любые символы">
                            </div>

                        </div>
                        <label for="userEmail" class="col-form-label">Почта:</label>
                        <input type="text" name="userEmail" class="form-control" id="userEmail"
                               value="${userToEdit.email}"
                               pattern="^([a-z0-9_-]+\.)*[a-z0-9_-]+@[a-z0-9_-]+(\.[a-z0-9_-]+)*\.[a-z]{2,6}$"
                               minlength="5" maxlength="30" placeholder="email@example.com">

                        <div class="row">
                            <div class="form-group col-md-6">
                                <label for="userName" class="col-form-label">Имя:</label>
                                <input type="text" name="userName" class="form-control" id="userName"
                                       value="${userToEdit.name}"
                                       pattern="^[a-zA-ZА-Яа-яЁё]+$"
                                       maxlength="30" placeholder="Допустимы любые буквы">
                            </div>
                            <div class="form-group col-md-6">
                                <label for="userSurname" class="col-form-label">Фамилия:</label>
                                <input type="text" name="userSurname" class="form-control" id="userSurname"
                                       value="${userToEdit.surname}"
                                       pattern="^[a-zA-ZА-Яа-яЁё]+$"
                                       maxlength="30" placeholder="Допустимы любые буквы">
                            </div>
                        </div>
                        <label for="userPhone" class="col-form-label">Телефон:</label>
                        <input type="tel" name="userPhone" class="form-control" id="userPhone"
                               value="${userToEdit.phone}"
                               pattern="^\+?[\s\-\(\)0-9]{7,19}$"
                               minlength="10" maxlength="24" placeholder="+38 050 555-55-55">

<%--                        <c:if test="${userToEdit.id == null}">--%>
                        <div class="row">
                            <div class="form-group col-md-6">
                                <label for="userLanguage">Язык:</label>
                                <select name="userLanguage" id="userLanguage" class="form-control">


                                    <option value="RU">Русский</option>
                                    <c:if test="${userToEdit.language == 'EN'}">
                                    <option value="EN" selected>Английский</option>
                                    </c:if>
                                    <c:if test="${userToEdit.language != 'EN'}">
                                        <option value="EN">Английский</option>
                                    </c:if>


                                </select>

                            </div>

                            <div class="form-group col-md-6">

                                <label for="userRole">Роль:</label>
                                <select name="userRole" id="userRole" class="form-control">
                                    <option value="USER">Пользователь</option>

                                    <c:if test="${userToEdit.role == 'ADMIN'}">
                                        <option value="ADMIN" selected>Администратор</option>
                                    </c:if>
                                    <c:if test="${userToEdit.role != 'ADMIN'}">
                                        <option value="ADMIN">Администратор</option>
                                    </c:if>





                                </select>

                            </div>
                            <div class="row">
                                <div class="form-group col-md-6">

                                    <label for="userNotification">Уведомления:</label>
                                    <select name="userNotification" id="userNotification" class="form-control">

                                        <option value="false">Выключено</option>

                                        <c:if test="${userToEdit.notification == 'true'}">
                                            <option value="true" selected>Включено</option>
                                        </c:if>
                                        <c:if test="${userToEdit.notification != 'true'}">
                                            <option value="true">Включено</option>
                                        </c:if>

                                    </select>

                                </div>

                                <div class="form-group col-md-6">
                                    <label for="userStatus">Статус:</label>
                                    <select name="userStatus" id="userStatus" class="form-control">

                                        <option value="BLOCKED">Заблокирован</option>

                                        <c:if test="${userToEdit.status == 'ACTIVE'}">
                                            <option value="ACTIVE" selected>Активен</option>
                                        </c:if>
                                        <c:if test="${userToEdit.status != 'ACTIVE'}">
                                            <option value="ACTIVE">Активен</option>
                                        </c:if>


                                    </select>
                                </div>

                            </div>
<%--                            </c:if>--%>

                        </div>
                    </div>
                    <div class="modal-footer">
                        <input type="hidden" name="command" value="adminRequest">
<%--                        <input type="hidden" name="userToEditId" value="${userToEdit.id}">--%>
                        <button type="submit" class="btn btn-primary" name="adminRequest" value="Add or edit user">
                            Submit
                        </button>

                        <button type="submit" class="btn btn-secondary"
                                data-bs-dismiss="modal"
                                name="adminRequest" value="Remove data"
                        >Cancel
                        </button>
                    </div>
            </form>
        </div>
    </div>
</div>

<script>
    var passwordValidation = function() {
        if (document.getElementById('userPassword').value.length < 5){
            document.getElementById('message').style.color = 'red';
            document.getElementById('message').innerHTML = 'Минимальная длина 5 символов';
            return;
        }
        if (document.getElementById('userPassword').value ==
            document.getElementById('confirmPassword').value) {
            document.getElementById('message').style.color = 'green';
            document.getElementById('message').innerHTML = 'Ок';
        } else {
            document.getElementById('message').style.color = 'red';
            document.getElementById('message').innerHTML = 'Пароли не совпадают';
        }
    }
</script>

<script>
    $(document).ready(function () {
        var hash = window.location.hash;
        if (hash == '#addOrEditUser') {
            $("#addOrEditUser").modal('show');
            history.pushState("", document.title, window.location.pathname
                + window.location.search);
        }
    })
</script>

</body>
</html>
