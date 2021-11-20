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
    <%--    <c:set var="currentUser" value="${sessionScope.user}"/>--%>


    <c:if test="${currentUser.role.value() != 'user'}">
        <c:redirect url="index.jsp"/>
    </c:if>


    <div class="page-header">
        <div class="row">
            <div class="col-md-8">
                <h1>Страница пользователя</h1>

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


    <input type="hidden" name="command" class="btn btn-primary" value="userRequest">
    <button type="submit" name="userRequest" class="btn btn-primary" value="Choice of services">Choice of services
    </button>

    <button type="button" class="btn btn-primary" data-bs-toggle="modal"
            data-bs-target="#">
        Edit profile
    </button>

    <button type="button" class="btn btn-primary" data-bs-toggle="modal"
            data-bs-target="#">
        Payment history
    </button>


</form>
<hr>
</body>
</html>
