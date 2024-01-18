<%@ page import="com.mainproject.be28.item.dto.ItemDto" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Base64" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false"%>

<%
    String accessKey = (String) request.getAttribute("accessKey");
    String secretKey = (String) request.getAttribute("secretKey");
    String itemsJson = (String) request.getAttribute("items");

    // Encode values
    String encodedAccessKey = Base64.getEncoder().encodeToString(accessKey.getBytes());
    String encodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());


%>
<script>
    var accessKey = atob('<%= encodedAccessKey %>');
    var secretKey = atob('<%= encodedSecretKey %>');
    var items  = <%= itemsJson %>;

    for (var i = 0; i < items.length; i++) {
        console.log(items[i].name);
    }
</script>

<%@ include file="/WEB-INF/views/menu.jsp" %>
<html>
<head>
    <title>상품목록</title>
    <link rel="stylesheet" href="<c:url value='/css/imageView.css'/>">
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="https://sdk.amazonaws.com/js/aws-sdk-2.1151.0.js"></script>
</head>
<body>

<div style="text-align:center; margin: 30px;">
    <h1>상품 목록</h1>
</div>
<table>
<div id="imageContainer">
    <script type="text/javascript" src="/js/aws_config.js"></script>
</div>

</table>
<div id="pageNation"></div>

</body>
</html>