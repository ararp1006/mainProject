<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="/WEB-INF/views/menu.jsp" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>주문 생성</title>
</head>
<body>

    <div class="container">
        <h2>주문 생성</h2>
        <form action="/submit-order" method="post">
            <!-- 주문자 정보 -->
            <label for="addressee">받는 사람</label>
            <input type="text" id="addressee" name="addressee" required>

            <label for="zipCode">우편번호</label>
            <input type="text" id="zipCode" name="zipCode" required>

            <label for="simpleAddress">주소</label>
            <input type="text" id="simpleAddress" name="simpleAddress" required>

            <label for="detailAddress">상세 주소</label>
            <input type="text" id="detailAddress" name="detailAddress">

            <label for="phoneNumber">전화번호</label>
            <input type="text" id="phoneNumber" name="phoneNumber" required>

            <!-- 상품 목록 -->
            <h3>상품 목록</h3>
            <ul id="orderItemList">
                <!-- 여기에 주문된 상품 목록이 동적으로 추가될 수 있습니다. -->
                <!-- 예시: <li>상품명 x 수량</li> -->
            </ul>

            <!-- 주문 총액 -->
            <label for="totalPrice">총 주문 금액</label>
            <input type="text" id="totalPrice" name="totalPrice" readonly>

            <!-- 결제 페이지를 포함하는 부분 -->
        </form>
    </div>
</body>
</html>
