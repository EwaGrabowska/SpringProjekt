<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="/resources/css/style.css"/>
    <title><s:message code="menu.files"/></title>
    <script type="text/javascript">
        function changeTrBg(row) {
            row.style.backgroundColor = "#e6e6e6";
        }

        function defaultTrBg(row) {
            row.style.backgroundColor = "#f0f8ff";
        }

        function startSerach(pParam) {
            var searchWord = document.getElementById('searchString').value;
            var page = parseInt(document.getElementById('cp').value) + parseInt(pParam);

            if (searchWord.length < 1) {
                document.getElementById("errorSearch").innerHTML = "<s:message code="error.searchString.toShort"/>";
                return false;
            } else {
                document.getElementById("errorSearch").innerHTML = "";
                var searchLink = '${pageContext.request.contextPath}/admin/files/search/' + searchWord + '/' + page;
                window.location.href = searchLink;
            }
        }

    </script>
</head>
<body>
<%@include file="/WEB-INF/incl/menu.app" %>
<%@include file="/WEB-INF/incl/admenu.app" %>
<h2><s:message code="menu.files"/></h2>

<sf:form id="uploadForm" action="${pageContext.request.contextPath}/admin/files/upload"
         method="POST" modelAttribute="fileupload" enctype="multipart/form-data">
    <input type="file" name="filename" id="filename"/>
    <input type="submit" value="<s:message code="button.upload"/>"/>
</sf:form>

<c:set var="licznik" value="${recordStartCounter }"/>
<div align="center">
    <div align="right" style="width: 1000px; padding: 2px;">
        <input type="hidden" name="cp" id="cp" value="${currentPage}"/>
        <input type="text" id="searchString"/>&nbsp;&nbsp;<input type="button" value="<s:message code="button.search"/>"
                                                                 onclick="startSerach(0);"/><br/>
        <span id="errorSearch" style="color: red;"></span>
    </div>
    <table width="1000" border="0" cellpadding="6" cellspacing="2" bgcolor="#f0f8ff">
        <tr bgcolor="#66CC99">
            <td width="50" align="center"></td>
            <td width="225" align="center"><b><s:message code="file.platformNumber"/></b></td>
            <td width="325" align="center"><b><s:message code="file.creationDate"/></b></td>
            <td width="100" align="center"><b><s:message code="file.projectName"/></b></td>
            <td width="100" align="center"><b><s:message code="file.nameOfPrincipalInvestigator"/></b></td>
            <td width="100" align="center"><b><s:message code="file.featureType"/></b></td>

            <td width="50" align="center"><b><s:message code="admin.file.delete"/></b></td>
            <td width="50" align="center"><b><s:message code="file.download"/></b></td>
        </tr>
        <c:forEach var="f" items="${fileList }">
            <c:set var="licznik" value="${licznik+1}"/>
            <tr onmouseover="changeTrBg(this)" onmouseout="defaultTrBg(this)">
                <td align="center"><c:out value="${licznik }"/></td>
                <td align="left"><a href="edit/${f.id }"><c:out value="${f.platformNumber }"/></a></td>
                <td align="left"><a href="edit/${f.id }"><c:out value="${f.creationDate }"/></a></td>
                <td align="center"><a href="edit/${f.id }"><c:out value="${f.projectName }"/></a></td>
                <td align="center"><a href="edit/${f.id }"><c:out value="${f.nameOfPrincipalInvestigator }"/></a></td>
                <td align="center"><a href="edit/${f.id }"><c:out value="${f.featureType }"/></a></td>
                <td align="center">
                    <a href="delete/${f.id }">
                        <img src="/resources/images/delete.jpg" width="16" height="16"
                             title="<s:message code="admin.file.delete"/>"/>
                    </a>
                </td>
                <td align="center">
                    <a href="download/${f.id }">
                        <img src="/resources/images/download.jpg" width="16" height="16"
                             title="<s:message code="file.download"/>"/>
                    </a>
                </td>
            </tr>
        </c:forEach>

    </table>
    <table width="1000" border="0" cellpadding="6" cellspacing="0" bgcolor="#66CC99">
        <tr>
            <td width="300" align="left">
                <s:message code="info.page"/>&nbsp${currentPage}&nbsp<s:message code="info.from"/>&nbsp${totalPages}
            </td>
            <td align="right">
                <c:if test="${currentPage > 1}">
                    <input type="button"
                           onclick="window.location.href='${pageContext.request.contextPath}/admin/files/${currentPage - 1}'"
                           value="<s:message code="link.poprzedni"/>"/>&nbsp;&nbsp;
                </c:if>
                <c:if test="${currentPage < totalPages}">
                    <input type="button"
                           onclick="window.location.href='${pageContext.request.contextPath}/admin/files/${currentPage + 1}'"
                           value="<s:message code="link.nastepny"/>"/>
                </c:if>

            </td>
        </tr>
    </table>

</div>
</body>
</html>