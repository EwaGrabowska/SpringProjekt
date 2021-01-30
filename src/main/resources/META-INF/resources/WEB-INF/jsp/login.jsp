<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="pl">
<head>
    <meta  http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="/resources/css/style.css" />
    <title><s:message code="logowanie.pageName" /></title>
</head>
<body>
<%@include file="/WEB-INF/incl/menu.app"%>
<h2>
    <s:message code="logowanie.pageName" />
</h2>
<form id="loginForm" action="${contextPath}/login" method="POST">

    <table width="350" border="0" cellpadding="4" cellspacing="1"
           align="center">

        <tr>
            <td colspan="2" align="center">
                <span>${error}</span>
            </td>
        </tr>
        <tr>
            <td align="right" width="140">
                <s:message code="register.email" />
            </td>
            <td align="left">
                <input name="username" type="text" class="form-control" placeholder="<s:message code="register.email" />"
                       autofocus="true" />
            </td>
        </tr>
        <tr>
            <td align="right" width="140">
                <s:message code="register.password" />
            </td>
            <td align="left">
                <input name="password" type="password" class="form-control" placeholder="<s:message code="register.password" />" />
            </td>
        </tr>

        <tr>
            <td align="right" width="150">
            </td>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <td colspan="2" align="left"><input
                    type="submit" value="Zaloguj"/></td>
        </tr>
        <tr>
            <td align="right" width="150">
            <s:message code="logowanie.rejestracja"/>
            </td>
            <td align="left">
            <input type="button" value="Rejestracja" onclick="window.location.href='${pageContext.request.contextPath}/register'"/>

        </tr>

    </table>
</form>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
