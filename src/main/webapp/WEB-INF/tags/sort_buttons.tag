<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />


<form action="controller" method="post">
    <input type="hidden" name="command" value="sort">
    <button type="submit" name="sort" class="btn btn-secondary btn-sm" value="Sort services"><fmt:message
            key="index_jsp.link.sort_services"/></button>
    <button type="submit" name="sort" class="btn btn-secondary btn-sm" value="Sort tariffs by name"><fmt:message
            key="index_jsp.link.sort_tariffs_by_name"/></button>
    <button type="submit" name="sort" class="btn btn-secondary btn-sm" value="Sort tariffs by price"><fmt:message
            key="index_jsp.link.sort_tariffs_by_price"/></button>

</form>
<hr>