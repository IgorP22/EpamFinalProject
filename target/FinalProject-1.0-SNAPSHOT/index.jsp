<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="mylib" tagdir="/WEB-INF/tags" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
      integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
<link rel="stylesheet" href="css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/red_stars.css"/>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>

<%--<c:set var --%>


<!DOCTYPE html>
<html lang="${language}">
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to provider main page</title>
</head>


<header>


    <c:if test="${param.restoreLink != null}">
        ${param.restoreLink}
    </c:if>

    <div class="page-header">
        <div class="row">

            <div class="col-md-8">
                <h1><fmt:message key="index_jsp.link.welcome"/></h1>
                <h2><fmt:message key="index_jsp.link.our_services_and_prices"/></h2>
            </div>

            <div class="col-md-2">
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


                <button type="button" class="btn btn-success" data-bs-toggle="modal"
                        data-bs-target="#userLogin">
                    <fmt:message key="index_jsp.link.login"/>
                </button>
                <br>
                <a href="#forgotPassword" data-bs-toggle="modal">Забыли пароль?</a>
                <%--                <button type="button"  data-bs-toggle="modal"--%>
                <%--                        data-bs-target="#forgotPassword">--%>
                <%--                    <fmt:message key="index_jsp.link.login"/>--%>
                <%--                </button>--%>


            </div>
        </div>
    </div>
    <hr>
    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#emailModal">
        <fmt:message key="index_jsp.link.get_price_to_email"/>
    </button>
    <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#downloadModal">
        <fmt:message key="index_jsp.link.download_price"/>
    </button>
    <hr>
</header>

<body>

<%--<a href="http://localhost:8080/Final/price_list.txt" download="price_list.txt">Скачать txt</a>--%>
<%--<a href="http://localhost:8080/Final/price_list.pdf" download="price_list.pdf">Скачать pdf</a>--%>

<mylib:sort_buttons/>


<!-- Start of Table -->
<table class="table table-bordered">
    <thead>
    <tr class="table-active">
        <th><fmt:message key="index_jsp.link.name"/></th>
        <th><fmt:message key="index_jsp.link.description"/></th>
        <th><fmt:message key="index_jsp.link.price"/></th>
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


<!-- Кнопка-триггер модального окна -->
<%--<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#emailModal">--%>
<%--    <fmt:message key="index_jsp.link.get_price_to_email"/>--%>
<%--</button>--%>

<!-- Модальное окно -->
<div class="modal fade" id="emailModal" tabindex="-1" aria-labelledby="emailModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="emailModalLabel"><fmt:message
                        key="index_jsp.link.enter_email_and_format"/></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="controller" method="post">

                <div class="modal-body">

                    <div class="mb-3">
                        <div class="form-group required">
                            <label for="email" class="col-form-label"><fmt:message
                                    key="index_jsp.link.enter_email_here"/></label>

                            <input type="text" name="email" class="form-control" id="email"
                                   pattern="^([a-z0-9_-]+\.)*[a-z0-9_-]+@[a-z0-9_-]+(\.[a-z0-9_-]+)*\.[a-z]{2,6}$"
                                   minlength="5" placeholder="email@example.com" required>

                        </div>
                    </div>
                </div>

                <div class="modal-footer">
                    <input type="hidden" name="command" value="email">
                    <button type="submit" class="btn btn-primary" name="file" value="txt"><fmt:message
                            key="index_jsp.link.send_as"/>.txt
                    </button>
                    <button type="submit" class="btn btn-primary" name="file" value="pdf"><fmt:message
                            key="index_jsp.link.send_as"/>.pdf
                    </button>

                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message
                            key="index_jsp.link.cancel"/></button>
                </div>


            </form>
        </div>
    </div>
</div>

<script src='https://www.google.com/recaptcha/api.js?hl=${language}'></script>


<!-- Кнопка-триггер модального окна -->

<%--<button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#downloadModal">--%>
<%--    <fmt:message key="index_jsp.link.download_price"/>--%>
<%--</button>--%>

<!-- Модальное окно -->
<div class="modal fade" id="downloadModal" tabindex="-1" aria-labelledby="downloadModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="downloadModalLabel"><fmt:message
                        key="index_jsp.link.chose_file_format"/></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <div class="modal-footer">
                <form action="controller" method="post">
                    <input type="hidden" name="command" value="download">
                    <button type="submit" class="btn btn-primary" name="file" value="txt" data-bs-dismiss="modal"
                            aria-label="Close"><fmt:message
                            key="index_jsp.link.download_as"/>.txt
                    </button>
                    <button type="submit" class="btn btn-primary" name="file" value="pdf" data-bs-dismiss="modal"
                            aria-label="Close"><fmt:message
                            key="index_jsp.link.download_as"/>.pdf
                    </button>
                </form>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message
                        key="index_jsp.link.cancel"/></button>
            </div>
        </div>
    </div>
</div>


<!-- Модальное окно -->
<div class="modal fade" id="userNotExist" tabindex="-1" aria-labelledby="userNotExist" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><fmt:message key="index_jsp.link.user_does_not_exist"/></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message
                        key="index_jsp.link.close"/></button>
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
                <h5 class="modal-title"><fmt:message key="index_jsp.link.username_and_password_dont_match"/></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message
                        key="index_jsp.link.close"/></button>
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

<!-- Модальное окно -->
<div class="modal fade" id="wrongCaptcha" tabindex="-1" aria-labelledby="wrongCaptcha" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">You missed CAPTCHA.</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message
                        key="index_jsp.link.close"/></button>
            </div>
        </div>
    </div>
</div>


<script>
    $(document).ready(function () {
        var hash = window.location.hash;
        if (hash == '#wrongCaptcha') {
            $("#wrongCaptcha").modal('show');
            history.pushState("", document.title, window.location.pathname
                + window.location.search);
        }
    })
</script>

<div class="modal fade" id="success" tabindex="-1" aria-labelledby="success" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Email sent.</h5>
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
        if (hash == '#success') {
            $("#success").modal('show');
            history.pushState("", document.title, window.location.pathname
                + window.location.search);
        }
    })
</script>


<div class="modal fade" id="userLogin" tabindex="-1" aria-labelledby="success" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Login</h5>
            </div>
            <form action="controller" method="post" class="form-inline form-search pull-right">
                <div class="modal-body">
                    <div class="mb-3">
                        <label for="login" class="col-form-label">Имя пользователя:</label>
                        <input type="text" name="login" class="form-control" id="login"
                               minlength="5" required>
                        <label for="password" class="col-form-label">Введите пароль:</label>
                        <input type="password" name="password" class="form-control" id="password"
                               minlength="5" required>
                        <br>
                        <center>
                            <div class="g-recaptcha" data-sitekey="6Leyol4dAAAAAOU5_NFGfBK1X65cqjKR85mbXkHD"></div>
                        </center>
                    </div>
                </div>


                <div class="modal-footer">

                    <input type="hidden" name="command" value="login">

                    <button type="submit" class="btn btn-primary" value="login"><fmt:message
                            key="index_jsp.link.login"/></button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>

                </div>
            </form>
        </div>
    </div>
</div>


<script>
    $(document).ready(function () {
        var hash = window.location.hash;
        if (hash == '#userLogin') {
            $("#userLogin").modal('show');
            history.pushState("", document.title, window.location.pathname
                + window.location.search);
        }
    })
</script>

<div class="modal fade" id="forgotPassword" tabindex="-1" aria-labelledby="success" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Восстановление пароля</h5>
            </div>
            <form action="controller" method="post" class="form-inline form-search pull-right">
                <div class="modal-body">
                    <div class="mb-3">
                        <label for="userLoginToRestore" class="col-form-label">Имя пользователя:</label>
                        <input type="text" name="userLoginToRestore" class="form-control" id="userLoginToRestore"
                               minlength="5" required>

                        <label for="emailToRestore" class="col-form-label"><fmt:message
                                key="index_jsp.link.enter_email_here"/></label>
                        <input type="text" name="emailToRestore" class="form-control" id="emailToRestore"
                               pattern="^([a-z0-9_-]+\.)*[a-z0-9_-]+@[a-z0-9_-]+(\.[a-z0-9_-]+)*\.[a-z]{2,6}$"
                               minlength="5" placeholder="email@example.com" required>
                        <%--                        <center>--%>
                        <%--                            <div class="g-recaptcha" data-sitekey="6Leyol4dAAAAAOU5_NFGfBK1X65cqjKR85mbXkHD"></div>--%>
                        <%--                        </center>--%>
                    </div>
                </div>

                <div class="modal-footer">

                    <input type="hidden" name="command" value="forgotPassword">

                    <button type="submit" class="btn btn-primary" value="forgotPassword">Submit</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message
                            key="index_jsp.link.close"/></button>

                </div>
            </form>
        </div>
    </div>
</div>


<div class="modal fade" id="passwordRecovery" tabindex="-1" aria-labelledby="success" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Welcome to password recovery page ${userLoginToRecover}.</h5>
            </div>
            <form action="controller" method="post" class="form-inline form-search pull-right">
                <div class="modal-body">
                    <div class="mb-3">
                        <div class="row">

                            <div class="form-group col-md-6">
                                <label for="userNewPassword" class="col-form-label">Пароль:</label>
                                <input type="password" name="userNewPassword" class="form-control" id="userNewPassword"

                                       onkeyup='passwordValidation();'
                                       minlength="5" maxlength="150" placeholder="Любые символы" required>
                                <span style="font-size: smaller" id='message'></span>
                            </div>

                            <div class="form-group col-md-6">
                                <label for="confirmPassword" class="col-form-label">Повторите ввод пароля:</label>
                                <input type="password" name="confirmPassword" class="form-control"
                                       id="confirmPassword" onkeyup='passwordValidation();'

                                       minlength="5" maxlength="30" placeholder="Любые символы">
                            </div>

                        </div>

                    </div>
                    ${userLoginToRecover}
                    ${userToRecover.userId}
                    ${userToRecover.code}
                </div>


                <div class="modal-footer">

                    <input type="hidden" name="command" value="forgotPassword">
                    <input type="hidden" name="userToRecover" value="${userToRecover}">
                    <button type="submit" class="btn btn-primary" value="forgotPassword">Submit</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>

                </div>
            </form>
        </div>
    </div>
</div>

<script>
    var passwordValidation = function () {
        if (document.getElementById('userNewPassword').value.length < 5) {
            document.getElementById('message').style.color = 'red';
            document.getElementById('message').innerHTML = 'Минимальная длина 5 символов';
            return;
        }
        if (document.getElementById('userNewPassword').value ==
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
        if (hash == '#passwordRecovery') {
            $("#passwordRecovery").modal('show');
            history.pushState("", document.title, window.location.pathname
                + window.location.search);
        }
    })
</script>

<div class="modal fade" id="invalidLink" tabindex="-1" aria-labelledby="invalidLink" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Link is not valid.</h5>
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
        if (hash == '#invalidLink') {
            $("#invalidLink").modal('show');
            history.pushState("", document.title, window.location.pathname
                + window.location.search);
        }
    })
</script>

<div class="modal fade" id="noSuchRecordInDb" tabindex="-1" aria-labelledby="invalidLink" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">No such user or email in DB.</h5>
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
        if (hash == '#noSuchRecordInDb') {
            $("#noSuchRecordInDb").modal('show');
            history.pushState("", document.title, window.location.pathname
                + window.location.search);
        }
    })
</script>


</body>
</html>

