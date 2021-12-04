<%--suppress ALL --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="mylib" tagdir="/WEB-INF/tags" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
      integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<link rel="stylesheet" href="css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/red_stars.css"/>

<%@ include file="success.jspf" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>


<!DOCTYPE html>
<html lang="${language}">

<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to provider main page</title>
</head>
<body>

<header>
    <c:set var="currentUser" value="${sessionScope.currentUser}"/>

    <c:if test="${currentUser.role.value() != 'admin'}">
        <c:redirect url="index.jsp"/>
    </c:if>

    <%--    <mylib:ch_admin/>--%>

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
        <button type="submit" name="adminRequest" class="btn btn-primary" value="List of services and tariffs">List of
            services and tariffs
        </button>

        <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                data-bs-target="#addOrEditService">
            Add new service
        </button>

        <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                data-bs-target="#addOrEditTariff">
            Add new tariff
        </button>


        <a href="admin_users.jsp" class="btn btn-success" role="button">Users page</a>


    </form>

    <hr>

</header>
<body>


<c:set var="confirmationFlag" scope="session" value="false"/>
<c:set var="adminFlag" value="${sessionScope.adminFlag}"/>

<mylib:sort_buttons/>


<!-- Start of Table -->
<table class="table table-bordered">
    <caption>Servises and tariffs table</caption>
    <thead>
    <tr class="table-active">
        <th>Наименование пакета</th>
        <th>Описание</th>
        <th>Цена</th>
        <th></th>
        <th></th>
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
            <form action="controller" method="post">
                <input type="hidden" name="serviceId" value="${service.id}">
                <input type="hidden" name="command" class="btn btn-primary" value="adminRequest">
                <td>
                    <button type="submit" name="adminRequest" class="btn btn-secondary btn-sm"
                            value="Add or edit service">Edit service
                    </button>
                </td>
                <td>
                    <button type="submit" name="adminRequest" class="btn btn-danger btn-sm"
                            value="Delete service">Delete service
                    </button>
                </td>
            </form>
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


                    <form action="controller" method="post">

                        <input type="hidden" name="tariffId" value="${tariff.id}">

                        <input type="hidden" name="command" class="btn btn-primary" value="adminRequest">
                        <td>
                            <button type="submit" name="adminRequest" class="btn btn-secondary btn-sm"
                                    value="Add or edit tariff">Edit tariff
                            </button>
                        </td>
                        <td>
                            <button type="submit" name="adminRequest" class="btn btn-danger btn-sm"
                                    value="Delete tariff">Delete tariff
                            </button>
                        </td>

                    </form>

                </tr>
            </c:if>
        </c:forEach>
    </c:forEach>

    </tbody>
</table>
<%--</c:if>--%>


<!-- Модальное окно -->
<div class="modal fade" id="deleteServiceConfirmation" tabindex="-1" aria-labelledby="deleteServiceConfirmation"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Delete confirmation window</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                Are you sure you want to delete service?<br>
                Data can't be restored!!!
            </div>

            <div class="modal-footer">
                <form action="controller" method="post">
                    <input type="hidden" name="command" value="adminRequest">
                    <input type="hidden" name="confirmation" value="true">
                    <button type="submit" class="btn btn-danger" name="adminRequest" value="Delete service">Delete
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
        if (hash == '#deleteServiceConfirmation') {
            $("#deleteServiceConfirmation").modal('show');
            history.pushState("", document.title, window.location.pathname
                + window.location.search);
        }
    })
</script>


<!-- Модальное окно -->
<div class="modal fade" id="deleteTariffConfirmation" tabindex="-1" aria-labelledby="deleteTariffConfirmation"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Delete confirmation window</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                Are you sure you want to delete tariff?<br>
                Data can't be restored!!!
            </div>

            <div class="modal-footer">
                <form action="controller" method="post">
                    <input type="hidden" name="command" value="adminRequest">
                    <input type="hidden" name="confirmation" value="true">
                    <button type="submit" class="btn btn-danger" name="adminRequest" value="Delete tariff">Delete
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
        if (hash == '#deleteTariffConfirmation') {
            $("#deleteTariffConfirmation").modal('show');
            history.pushState("", document.title, window.location.pathname
                + window.location.search);
        }
    })
</script>


<script src="https://code.jquery.com/jquery-3.6.0.slim.min.js"></script>

<!-- Модальное окно -->
<div class="modal fade" id="addOrEditService" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
     aria-labelledby="addOrEditService" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Данные сервиса</h5>

            </div>
            <form action="controller" method="post">

                <div class="modal-body">
                    <c:set var="serviceToEdit" value="${serviceToEdit}"/>
                    <c:if test="${serviceToEdit.id != null}">

                        <input type="hidden" name="serviceId" value="${serviceToEdit.id}">
                    </c:if>

                    <div class="mb-3">
                        <label for="serviceNameRu" class="col-form-label">Название сервиса на русском языке:</label>
                        <input type="text" name="serviceNameRu" class="form-control" id="serviceNameRu"
                               value="${serviceToEdit.titleRu}"

                               minlength="5" maxlength="150" placeholder="Допустимы любые символы" required>
                        <label for="serviceNameEn" class="col-form-label">Название сервиса на английском языке:</label>
                        <input type="text" name="serviceNameEn" class="form-control" id="serviceNameEn"
                               pattern="^[^а-яА-ЯЁё]+" value="${serviceToEdit.titleEn}"
                               minlength="5" maxlength="150" placeholder="Любые символы кроме кирилицы" required>

                    </div>
                </div>
                <div class="modal-footer">
                    <input type="hidden" name="command" value="adminRequest">
                    <button type="submit" class="btn btn-primary" name="adminRequest" value="Add or edit service">
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
    $(document).ready(function () {
        var hash = window.location.hash;
        if (hash == '#addOrEditService') {
            $("#addOrEditService").modal('show');
            history.pushState("", document.title, window.location.pathname
                + window.location.search);
        }
    })
</script>


<div class="modal fade" id="addOrEditTariff" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
     aria-labelledby="addOrEditTariff" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Данные тарифа</h5>

            </div>
            <form action="controller" method="post">

                <div class="modal-body">
                    <c:set var="tariffToEdit" value="${tariffToEdit}"/>

                    <c:if test="${tariffToEdit.id != null}">
                        <c:set var="serviceName" value="${serviceListForTariff.titleRu}"/>
                        <input type="hidden" name="tariffId" value="${tariffToEdit.id}">
                        <input type="hidden" name="serviceId" value="${tariffToEdit.serviceId}">
                    </c:if>


                    <div class="mb-3">
                        <label for="tariffNameRu" class="col-form-label">Название тарифа на русском языке:</label>
                        <input type="text" name="tariffNameRu" class="form-control" id="tariffNameRu"
                               value="${tariffToEdit.nameRu}"
                               minlength="5" maxlength="150" placeholder="Допустимы любые символы" required>

                        <label for="tariffNameEn" class="col-form-label">Название тарифа на английском языке:</label>
                        <input type="text" name="tariffNameEn" class="form-control" id="tariffNameEn"
                               pattern="^[^а-яА-ЯЁё]+" value="${tariffToEdit.nameEn}"
                               minlength="5" maxlength="150" placeholder="Любые символы кроме кирилицы" required>

                        <label for="tariffPrice" class="col-form-label">Цена тарифа:</label>
                        <input type="text" name="tariffPrice" class="form-control" id="tariffPrice"
                        <%--                               pattern="^[^а-яА-ЯЁё]+"--%>
                               value="${tariffToEdit.price}"
                               minlength="1" maxlength="10" placeholder="Любые символы кроме кирилицы" required>

                        <label for="tariffDescriptionRu" class="col-form-label">Описание тарифа на русском
                            языке:</label>
                        <input type="text" name="tariffDescriptionRu" class="form-control" id="tariffDescriptionRu"
                               value="${tariffToEdit.descriptionRu}"
                               minlength="5" maxlength="3000" placeholder="Допустимы любые символы" required>

                        <label for="tariffDescriptionEn" class="col-form-label">Описание тарифа на английском
                            языке:</label>
                        <input type="text" name="tariffDescriptionEn" class="form-control" id="tariffDescriptionEn"
                               pattern="^[^а-яА-ЯЁё]+" value="${tariffToEdit.descriptionEn}"
                               minlength="5" maxlength="3000" placeholder="Любые символы кроме кирилицы" required>
                        <c:if test="${tariffToEdit.id != null}">
                            <div class="form-group">
                                <label for="disabledSelect">Сервис</label>
                                <select id="disabledSelect" class="form-control" disabled>

                                    <option>${serviceName}</option>
                                </select>
                            </div>
                        </c:if>

                        <c:if test="${tariffToEdit.id == null}">
                            <div class="form-group">
                                <label for="serviceIdForTariff">Сервис</label>
                                <select name="serviceIdForTariff" id="serviceIdForTariff" class="form-control">

                                    <c:forEach var="serviceList" items="${ListOfServices}">
                                        <option value="${serviceList.id}">${serviceList.titleRu}</option>

                                    </c:forEach>
                                </select>
                            </div>
                        </c:if>


                    </div>
                </div>
                <div class="modal-footer">
                    <input type="hidden" name="command" value="adminRequest">
                    <button type="submit" class="btn btn-primary" name="adminRequest" value="Add or edit tariff">
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
    $(document).ready(function () {
        var hash = window.location.hash;
        if (hash == '#addOrEditTariff') {
            $("#addOrEditTariff").modal('show');
            history.pushState("", document.title, window.location.pathname
                + window.location.search);
        }
    })
</script>


</body>




