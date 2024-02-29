<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.mainproject.be28.cart.dto.CartDto" %>
<%@ page import="com.mainproject.be28.cart.dto.CartItemDto" %>

<%@ include file="/WEB-INF/views/menu.jsp" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }

        header {
            background-color: #333;
            color: #fff;
            text-align: center;
            padding: 10px;
        }

        main {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #333;
            color: #fff;
        }

        .cart-total {
            font-weight: bold;
            text-align: right;
        }

        button {
            background-color: #4caf50;
            color: #fff;
            padding: 10px 15px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }
    </style>

     <script>
            function redirectToOrderPage() {
                window.location.href = "/order";
            }
        </script>
</head>
<body>
    <header>
        <h1>장바구니</h1>
    </header>
    <main>
        <table>
            <thead>
                <tr>
                    <th>상품명</th>
                    <th>갯수</th>
                    <th>가격</th>
                    <th>총가격</th>
                </tr>
            </thead>
             <tbody>
                 <c:forEach var="cartItem" items="${cartItems}">
                                    <tr>
                                        <td>${cartItem.item.name}</td>
                                        <td>${cartItem.quantity}</td>
                                        <td>$${cartItem.item.price}</td>
                                        <td>$${cartItem.quantity * cartItem.item.price}</td>
                                    </tr>
                 </c:forEach>
                        </tbody>
                    </table>
                    <div class="cart-total">
                        <p>결제 금액: $${totalAmount}</p>
                    </div>
         <button onclick="redirectToOrderPage()">주문하기</button>
    </main>
</body>
</html>