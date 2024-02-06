<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>마이페이지 🛒TechComputeMall</title>
    <style>
        body, h1, h2, section, span {
            margin: 0;
            padding: 0;
        }

        body {
            font-family: Arial, sans-serif;
        }

        #my-page-content {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background-color: #f8f8f8;
            border: 1px solid #ddd;
            border-radius: 8px;
        }

        h1, h2 {
            color: #333;
        }

        #cart, #order-history, #my-info {
            margin-bottom: 20px;
            padding: 15px;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 8px;
        }

        #user {
            display: block;
            margin-top: 10px;
        }

        #name-value, #email-value {
            font-weight: bold;
            color: #007bff;
        }


    </style>
</head>
<body>
<%@ include file="/WEB-INF/views/menu.jsp" %>
<div id="my-page-content">
    <h1>마이페이지</h1>

    <section id="cart">
        <h2>장바구니</h2>
    </section>

    <section id="order-history">
        <h2>주문 내역</h2>
    </section>

    <section id="my-info">
            <h1>사용자 정보</h1>

        <span id="user">
        이름: <span id="name-value"></span>
        이메일: <span id="email-value"></span>
    </span>
        <button type="button" onclick="updateMemberForm()">사용자 정보 수정</button>
    </section>
</div>

</div>
</div>


<script>

    // 토큰이 있다면 디코딩
    if (access_token) {
        const decodedToken = parseJwt(access_token);

        // 클레임에서 원하는 정보 추출
        const username = decodedToken.name;
        const email = decodedToken.email;
        console.log(decodedToken);
        console.log('Username:', username);

        if (username) {
            console.log('Setting username and email in HTML:', username, email);
            document.getElementById('name-value').innerText = username;
            document.getElementById('email-value').innerText = email;

        }else {
            console.log('토큰이 없습니다.');
            document.getElementById('login-text').innerText = '로그인';
        }
    }

    // JWT 토큰 디코딩 함수
    function parseJwt(token) {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        return JSON.parse(jsonPayload);
    }

        function updateMemberForm() {
            // 페이지 리다이렉션
            window.location.href = '/updateMemberForm';

    }


</script>

</body>
</html>
