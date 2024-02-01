<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<%@ include file="/WEB-INF/views/menu.jsp" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script type="text/javascript" src="/js/signUp.js"></script>
    <link rel="stylesheet" href="<c:url value='/css/signUp.css'/>">

</head>
<body>

<div class="sign" action="/members/signup" method="post">
    <h2>회원가입</h2>
<BR><BR>
    <p>
        <label for="email">이메일
        <input type="email" id="email" name="email" required>
        <input type="button" value="중복확인"onclick="checkEmail()"></label>
    </p>
    <label for="name">이름</label>
    <input type="text" id="name" name="name" required>

    <label for="password">비밀번호</label>
    <input type="password" id="password" name="password" required>

    <button onclick="submitFormSignUp()">가입하기</button>
</div>

</body>
</html>
