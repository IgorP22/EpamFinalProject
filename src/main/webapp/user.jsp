<%--suppress ELValidationInJSP --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="javaTag" uri="http://com.podverbnyj.provider.web" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
      integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="https://cdn.datatables.net/1.11.3/js/dataTables.bootstrap5.min.js"></script>
<script src="https://cdn.datatables.net/1.11.3/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.11.3/css/dataTables.bootstrap5.min.css"></script>
<link rel="stylesheet" href="css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/tables.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css"/>

<%@ include file="success.jspf" %>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}"
       scope="session"/>
<fmt:setLocale value="${language}"/>


<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User page</title>
</head>


<header>


    <c:if test="${currentUser.role.value() != 'user'}">
        <c:redirect url="index.jsp"/>
    </c:if>

    <div class="page-header">
        <div class="row">
            <div class="col-md-4">
                <h1>Страница пользователя</h1>
            </div>

            <div class="col-md-3">
                <div class="row align-items-center">
                    <h5>Добро пожаловать</h5><br>
                    <c:if test="${currentUser.name == null}">
                        <h4>
                                ${currentUser.login}
                        </h4>
                    </c:if>

                    <c:if test="${currentUser.name != null}">
                        <h4>
                                ${currentUser.name} ${currentUser.surname}
                        </h4>

                    </c:if>

                </div>

            </div>
            <div class="col-md-3">
                <h5>Общая стоимость подключенных услуг:</h5>
                <h3>${totalCost} грн</h3><h5>(за период 30 дней)</h5>
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
                <form>
                    <input type="button" value="Logout" class="btn btn-primary" onClick='location.href="index.jsp"'>
                </form>
            </div>
        </div>
    </div>


<hr>

<div class="row">
    <div class="col-md-5">
        <form action="controller" method="post">
            <input type="hidden" name="command" class="btn btn-primary" value="userRequest">
            <button type="submit" name="userRequest" class="btn btn-primary" value="Choice of services">Choice of
                services
            </button>
            <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                    data-bs-target="#addOrEditUser">
                Edit profile
            </button>
            <input type="hidden" name="command" class="btn btn-primary" value="userRequest">
            <button type="submit" class="btn btn-primary" name="userRequest" value="Payment history">
                Payment history
            </button>
        </form>
    </div>
    <div class="col-md-2">
        <h4>
            <c:set var="color" value="color:darkred"/>
            <c:if test="${currentUser.status == 'ACTIVE'}">
                <c:set var="color" value="color:green"/>
            </c:if>
            <span style="${color}">Status: ${currentUser.status}</span>
        </h4>
    </div>
    <div class="col-md-2">
        <h4>
            <c:set var="color" value="color:red"/>
            <c:if test="${currentUser.balance > 0}">
                <c:set var="color" value="color:green"/>
            </c:if>
            <span style="${color}">Your balance: ${currentUser.balance}</span>
        </h4>
    </div>
    <div class="col-md-3">
        <form action="controller" method="post">
            <div class="row">
                <div class="col-auto">
                    <input type="number" class="form-control"
                           pattern="^\d?\d\.\d\d$" name="sum"
                           min="1" step="0.01" id="sum" placeholder="Сумма пополнения">
                </div>
                <div class="col-auto">
                    <input type="hidden" name="command" value="userRequest">
                    <input type="hidden" name="userToEditId" value="${currentUser.id}">
                    <button type="submit" value="Edit balance" class="btn btn-success" name="userRequest">Пополнить
                    </button>
                </div>
            </div>
        </form>
    </div>
</div>


<hr>

</header>

<%--<javaTag:add x="2" y="5"/>--%>


<body>



<c:set var="paymentsList" value="${userPaymentsList}"/>
<c:set var="flag" value="${userFlag}"/>

<c:if test="${flag == 'History'}">

    <h3>История пополнения и списаний со счета</h3>



    <table class="table table-bordered table_sort" id="paymentHistory">
        <thead>
        <tr class="table-active">
            <th data-order="-1" class="sorted">Дата и время</th>
            <th>Сумма</th>
        </tr>
        </thead>
        <tbody>

        <c:forEach var="history" items="${userPaymentsList}">

            <tr>
                <td>${history.date}</td>
                <td>${history.sum}</td>

            </tr>

        </c:forEach>

        </tbody>
    </table>

</c:if>


<%@ include file="sorter_table.jspf" %>

<%--<script>--%>
<%--    $(document).ready(function () {--%>
<%--        $('#paymentHistory').DataTable({--%>
<%--            "pagingType": "full_numbers"--%>
<%--        });--%>
<%--    });--%>

<%--</script>--%>

<c:if test="${flag == 'Choice'}">
    <form action="controller" method="post">
        <table class="table table-bordered">
            <thead>
            <tr class="table-active">

                <th>Наименование пакета</th>
                <th>Описание</th>
                <th>Цена</th>
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
                    <td></td>
                </tr>

                <c:forEach var="tariff" items="${ListOfTariffs}">
                    <c:if test="${service.id == tariff.serviceId}">
                        <tr>
                            <td>
                                <div class="form-check">
                                    <c:set var="flag" value=""/>

                                    <c:forEach var="userTariff" items="${userTariffList}">
                                        <c:if test="${tariff.id == userTariff.tariffId}">
                                            <c:set var="flag" value="checked"/>

                                        </c:if>

                                    </c:forEach>

                                    <input class="form-check-input" type="checkbox" name="${service.id}"
                                           id="${service.id}" value="${tariff.id}" onclick="onlyOne(this)" ${flag}>

                                    <label class="form-check-label" for="${service.id}">

                                    </label>

                                </div>
                            </td>
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

        <input type="hidden" name="command" class="btn btn-primary" value="userRequest">
        <button type="submit" name="userRequest" class="btn btn-primary" value="Update services">Update services
        </button>
    </form>
</c:if>

<script>
    function onlyOne(checkbox) {
        document.getElementsByName(checkbox.name).forEach(n => {
            n.checked = n === checkbox ? n.checked : false;
        });
    }

</script>


<%@ include file="add_or_edit_user.jspf" %>

</body>
</html>
