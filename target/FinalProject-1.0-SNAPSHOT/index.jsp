<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
      integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
<link rel="stylesheet" href="css/boo">

<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />


<!DOCTYPE html>
<html lang="${language}">
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to provider main page</title>
</head>
<body>

<header>


        <div class="page-header">
            <div class="row">

                <div class="col-md-8">
                    <h1>Добро пожаловать</h1>
                    <h2>Наши улуги и цены</h2>
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

                </div>

                <div class="col-md-2">
                    <div class="row align-items-center">
                        <form action="controller" method="post" class="form-inline form-search pull-right">
                            <input type="hidden" name="command" value="login">
                            <input name="login"><br>
                            <input type="password" name="password"><br>
                            <button type="submit" class="btn btn-primary" value="Login">Login</button>
                        </form>
                    </div>

                </div>
            </div>
        </div>

</header>


<fmt:message key="index_jsp.link.settings"/>
${language}



<hr>
<form action="controller" method="post">
    <input type="hidden" name="command" value="sort">
    <button type="submit" name="sort" class="btn btn-primary" value="Sort services">Sort services</button>
    <button type="submit" name="sort" class="btn btn-primary" value="Sort tariffs by name">Sort tariffs by name</button>
    <button type="submit" name="sort" class="btn btn-primary" value="Sort tariffs by price">Sort tariffs by price</button>

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
            <c:if test="${language=='ru'}">
            <td>${service.titleRu}</td>
            </c:if>

            <c:if test="${language=='en'}">
                <td>${service.titleEn}</td>
            </c:if>

            <td></td>
            <td></td>
        </tr>

        <c:forEach var="tariff" items="${ListOfTariffs}">
            <c:if test="${service.id == tariff.serviceId}">
                <tr>
                    <c:if test="${language=='ru'}">
                        <td>${tariff.nameRu}</td>
                        <td>${tariff.descriptionRu}</td>
                    </c:if>
                    <c:if test="${language=='en'}">
                        <td>${tariff.nameEn}</td>
                        <td>${tariff.descriptionEn}</td>
                    </c:if>



                    <td>${tariff.price}</td>
                </tr>
            </c:if>
        </c:forEach>
    </c:forEach>

    </tbody>
</table>


<!--<a href="hello-servlet">Hello Servlet</a> -->


<!-- Кнопка-триггер модального окна -->
<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#emailModal">
    Get price list by email
</button>

<!-- Модальное окно -->
<div class="modal fade" id="emailModal" tabindex="-1" aria-labelledby="emailModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="emailModalLabel">Enter email and chose file format</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="controller" method="post">

                <div class="modal-body">

                    <div class="mb-3">
                        <label for="email" class="col-form-label">Enter e-mail here:</label>
                        <input type="text" name="email" class="form-control" id="email"
                               pattern="^([a-z0-9_-]+\.)*[a-z0-9_-]+@[a-z0-9_-]+(\.[a-z0-9_-]+)*\.[a-z]{2,6}$"
                               minlength="5" placeholder="email@example.com" required>


                    </div>
                </div>

                <div class="modal-footer">
                    <input type="hidden" name="command" value="email">
                    <button type="submit" class="btn btn-primary" name="file" value="txt">Download as .txt</button>
                    <button type="submit" class="btn btn-primary" name="file" value="pdf">Download as .pdf</button>

                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                </div>
            </form>
        </div>
    </div>
</div>


<!-- Кнопка-триггер модального окна -->
<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#downloadModal">
    Download prise list
</button>

<!-- Модальное окно -->
<div class="modal fade" id="downloadModal" tabindex="-1" aria-labelledby="downloadModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="downloadModalLabel">Chose file format</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <div class="modal-footer">
                <form action="controller" method="post">
                    <input type="hidden" name="command" value="download">
                    <button type="submit" class="btn btn-primary" name="file" value="txt">Download as .txt</button>
                    <button type="submit" class="btn btn-primary" name="file" value="pdf">Download as .pdf</button>
                </form>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
            </div>
        </div>
    </div>
</div>


<!-- Модальное окно -->
<div class="modal fade" id="userNotExist" tabindex="-1" aria-labelledby="userNotExist" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">User does not exist</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.slim.min.js"></script>

<!-- Скрипт, вызывающий модальное окно после загрузки страницы -->
<script>
    $(document).ready(function () {
        var hash = window.location.hash;
        if (hash == '#userNotExist') {
            $("#userNotExist").modal('show');
            history.pushState("", document.title, window.location.pathname
                + window.location.search);
        }
    })
</script>

<!-- Модальное окно -->
<div class="modal fade" id="wrongPassword" tabindex="-1" aria-labelledby="wrongPassword" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Username and password don't match</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>


<script>
    $(document).ready(function () {
        var hash = window.location.hash;
        if (hash == '#wrongPassword') {
            $("#wrongPassword").modal('show');
            history.pushState("", document.title, window.location.pathname
                + window.location.search);
        }
    })
</script>


</body>
</html>

