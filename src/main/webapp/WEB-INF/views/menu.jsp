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
            <a href="/loginForm" onclick="toggleLoginStatus()">
                <span id="login-text"></span>
            </a>
            <span id="user-name" style="display: none;" >
                <span id="user-name-value">
                </span>님
            </span>
        </li>
        <li id="admin" style="display: none;">
            <a href="/itemUpload">관리자페이지</a>
        </li>
        <li><a href=""><i class="fa fa-search"></i></a></li>
    </ul>
</div>



<script>
    // access_token 값을 가져오기
    const access_token = localStorage.getItem('access_token');
    console.log("access_token= " + access_token);

    // 토큰이 있다면 디코딩
    if (access_token) {
        const decodedToken = parseJwt(access_token);

        // 클레임에서 원하는 정보 추출
        const username = decodedToken.name;
        const role = decodedToken.roles;
        const email = decodedToken.email;

        console.log('사용자 이름:', username);
        console.log('역할',role)


        // 수정: 사용자 이름이 있을 때만 보이도록 변경
        if (username) {
            document.getElementById('login-text').innerText = '로그아웃';
            document.getElementById('user-name').style.display = 'inline';
            document.getElementById('user-name-value').innerText = username;
            document.getElementById('user-name-value').addEventListener('click', function() {
                window.location.href = '/myPage';
            });
            // 추가: 사용자가 admin일 때만 상품등록하기 메뉴 표시
            if (role.includes('ADMIN')) {
                document.getElementById('admin').style.display = 'inline';
            }
        }
    } else {
        console.log('토큰이 없습니다.');
        document.getElementById('login-text').innerText = '로그인';

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


    //  로그인/로그아웃 토글 함수
    function toggleLoginStatus() {
        if (access_token) {
            localStorage.removeItem('access_token');
            console.log('로그아웃');
        } else {
            //
            console.log('로그인');
        }

        // 페이지 리로딩
        location.reload();
    }
</script>

</body>
</html>
