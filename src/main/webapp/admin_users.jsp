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


<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />

<%@ include file="success.jspf" %>



<!DOCTYPE html>
<html lang="${language}">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><fmt:message key="admin.users.admin_users"/></title>
</head>


<header>
    <c:set var="currentUser" value="${sessionScope.user}"/>


    <c:if test="${currentUser.role.value() != 'admin'}">
        <c:redirect url="index.jsp"/>
    </c:if>

    <div class="page-header">
        <div class="row">
            <div class="col-md-8">
                <h1><fmt:message key="admin.admin_page"/></h1>

            </div>
            <div class="col-md-2">
                <div class="row align-items-center">
                    <h5><fmt:message key="index_jsp.link.welcome"/></h5><br>
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

        <a href="admin.jsp" class="btn btn-primary" role="button"><fmt:message key="admin.list_of_services_and_tariffs"/></a>

        <button type="submit" name="adminRequest" class="btn btn-success" value="List of users"><fmt:message key="admin.users.list_of_users"/></button>

        <button type="button" class="btn btn-success" data-bs-toggle="modal"
                data-bs-target="#addOrEditUser">
            <fmt:message key="admin.users.add_new_user"/>
        </button>

        <button type="submit" name="adminRequest" class="btn btn-success" value="Email to all user">Почта></button>



    </form>
    <hr>




</header>
<body>

<div class="form-group">
    <input type="text" class="form-control pull-right" id="search" placeholder="Поиск по таблице">
</div>


<table class="table table-striped table_sort" id="usersTable">
    <caption hidden>Table of users</caption>
    <thead>
    <tr class="table-active">
        <th id = "field 01"><fmt:message key="admin.users.login"/></th>
        <th id = "field 02"><fmt:message key="admin.users.total_cost"/></th>
        <th id = "field 03"><fmt:message key="admin.users.email"/></th>
        <th id = "field 04"><fmt:message key="admin.users.name"/></th>
        <th id = "field 05"><fmt:message key="admin.users.surname"/></th>
        <th id = "field 06"><fmt:message key="admin.users.phone"/></th>
        <th id = "field 07"><fmt:message key="admin.users.balance"/></th>
        <th id = "field 08"><fmt:message key="admin.users.language"/></th>
        <th id = "field 09"><fmt:message key="admin.users.role"/></th>
        <th id = "field 10"><fmt:message key="admin.users.notification"/></th>
        <th id = "field 11"><fmt:message key="admin.users.status"/></th>
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
                    <td><fmt:message key="add_or_edit_user.russian"/></td>
                </c:when>
                <c:otherwise>
                    <td><fmt:message key="add_or_edit_user.english"/></td>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${currentUser.role == 'ADMIN'}">
                    <td><fmt:message key="add_or_edit_user.admin"/></td>
                </c:when>
                <c:otherwise>
                    <td><fmt:message key="add_or_edit_user.user"/></td>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${currentUser.notification == 'false'}">
                    <td><fmt:message key="add_or_edit_user.off"/></td>
                </c:when>
                <c:otherwise>
                    <td><fmt:message key="add_or_edit_user.on"/></td>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${currentUser.status == 'BLOCKED'}">
                    <td><fmt:message key="add_or_edit_user.blocked"/></td>
                </c:when>
                <c:otherwise>
                    <td><fmt:message key="add_or_edit_user.active"/></td>
                </c:otherwise>
            </c:choose>


            <form action="controller" method="post">

                <input type="hidden" name="userToEditId" value="${currentUser.id}">

                <input type="hidden" name="command" class="btn btn-primary" value="adminRequest">
                <td>
                    <button type="submit" name="adminRequest" class="btn btn-secondary btn-sm"
                            value="Add or edit user"><fmt:message key="admin.users.edit_user"/>
                    </button>
                </td>
                <td>

                    <c:if test="${currentUser.role == 'ADMIN'}">
                        <button type="submit" name="adminRequest" class="btn btn-danger btn-sm"
                                value="Delete user"><fmt:message key="admin.users.delete_user"/>
                        </button>
                    </c:if>
                    <c:if test="${currentUser.role == 'USER'}">
                        <c:if test="${currentUser.status == 'BLOCKED'}">
                            <button type="submit" name="adminRequest" class="btn btn-danger btn-sm"
                                    value="Unblock user"><fmt:message key="admin.users.unblock_user"/>
                            </button>
                        </c:if>
                        <c:if test="${currentUser.status == 'ACTIVE'}">
                            <button type="submit" name="adminRequest" class="btn btn-danger btn-sm"
                                    value="Block user"><fmt:message key="admin.users.block_user"/>
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
                <h5 class="modal-title"><fmt:message key="admin.users.delete_user_confirmation_window"/></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <fmt:message key="admin.users.are_you_sure"/><br>
                <fmt:message key="admin.delete_data_cant_restored"/>
            </div>

            <div class="modal-footer">
                <form action="controller" method="post">
                    <input type="hidden" name="command" value="adminRequest">
                    <input type="hidden" name="confirmation" value="true">
                    <button type="submit" class="btn btn-danger" name="adminRequest" value="Delete user"><fmt:message key="admin.delete"/>
                    </button>
                </form>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message key="admin.cancel"/></button>
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
                <h5 class="modal-title"><fmt:message key="admin.users.last_admin"/></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-bs-dismiss="modal"><fmt:message key="index_jsp.link.close"/></button>
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
