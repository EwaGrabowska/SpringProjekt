<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="/resources/css/style.css"/>
    <title><s:message code="menu.mainPage"/></title>
</head>
<body>
<%@include file="/WEB-INF/incl/menu.app" %>
<%@include file="/WEB-INF/incl/admenu.app" %>
<h3 align="center"><c:out value="${message }"/></h3>
<%@include file="/WEB-INF/incl/argo.app" %>

<sf:form id="files" action="${pageContext.request.contextPath}/files/1"
         method="GET" enctype="multipart/form-data">
    <button type="submit" style="font-size: large">
        <s:message code="user.file.download"/>
        <img src="/resources/images/download.jpg" width="20" height="20"/>
    </button>
</sf:form>
</body>
</html>