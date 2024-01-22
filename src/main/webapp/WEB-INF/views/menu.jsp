<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
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
        <li><a href="<c:url value='/item/itemsPage'/>">상품보러가기</a></li>
        <li id="login-logout">
            <!-- 수정: onclick 이벤트에서 함수 호출 -->
            <a href="/loginForm" onclick="toggleLoginStatus()">
                <!-- 수정: 스크립트에서 로그인 상태를 확인하여 텍스트 변경 -->
                <span id="login-text"></span>
            </a>
            <!-- 추가: 로그인 상태일 때만 토큰 표시 -->
            <span id="token-display" style="display: none;">
                토큰: <span id="access-token"></span>
            </span>
        </li>
        <li><a href="<c:url value='${loginOutLink}'/>">${loginOut}</a></li>
        <li><a href=""><i class="fa fa-search"></i></a></li>
    </ul>
</div>

<script>
    // 페이지 로딩 시 access_token 값을 가져와서 변수에 할당
    const access_token = localStorage.getItem('access_token');
    console.log("access_token= " + access_token);

    // 페이지 로딩 시 실행되는 함수
    window.onload = function() {
        // 수정: access_token이 있으면 로그아웃 상태로 텍스트 변경
        if (access_token) {
            document.getElementById('login-text').innerText = '로그아웃';
        } else {
            document.getElementById('login-text').innerText = '로그인';
        }

        // 추가: access_token이 있으면 토큰 표시
        if (access_token) {
            document.getElementById('token-display').style.display = 'inline';
            document.getElementById('access-token').innerText = access_token;
        }
    };

    // 수정: 로그인/로그아웃 토글 함수
    function toggleLoginStatus() {
        if (access_token) {
            // access_token이 있으면 로그아웃 처리
            localStorage.removeItem('access_token');
            console.log('로그아웃');
        } else {
            // access_token이 없으면 로그인 처리
            // 여기에 로그인 처리에 필요한 추가 코드 작성
            console.log('로그인');
        }

        // 페이지 리로딩
        location.reload();
    }
</script>

</body>
</html>
