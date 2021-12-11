<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<head>
    <title><fmt:message key="error_page_title"/></title>
</head>

<body>

<h2><fmt:message key="error_page_error"/></h2>

${ex.message}

</body>

