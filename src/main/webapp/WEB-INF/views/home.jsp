<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>🛒 TechComputeMall</title>
    <link rel="stylesheet" href="<c:url value='/css/menu.css'/>">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css"/>
</head>
<body>
<div id="menu">
    <ul>
        <li id="logo"><a href="<c:url value='/home'/>">TechComputeMall</a></li>
        <li><a href="<c:url value='//'/>">문의하기</a></li>
        <li><a href="<c:url value='${loginOutLink}'/>">${loginOut}</a></li>
        <li><a href="<c:url value='/itemUpload'/>">이미지 업로드 하기</a></li>
        <li><a href=""><i class="fa fa-search"></i></a></li>
    </ul>
</div>
<div style="text-align:center">
    <img src="static/images/computer.png" alt="computer1">



</div>
</div>
</body>
</html>
