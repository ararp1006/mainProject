<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/WEB-INF/views/menu.jsp" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }

        form {
            width: 300px;
            margin: 50px auto;
        }

        label {
            display: block;
            margin-bottom: 5px;
        }

        input {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
        }

        button {
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }
    </style>
</head>
<body>

<form action="/members/signup" method="post"  enctype="application/json">
    <h2>회원가입</h2>
    <label for="name">사용자명:</label>
    <input type="text" id="name" name="name" required>

    <label for="email">이메일:</label>
    <input type="email" id="email" name="email" required>

    <label for="password">비밀번호:</label>
    <input type="password" id="password" name="password" required>

    <button type="submit">가입하기</button>
</form>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>

<script>
    function submitSignupForm() {
        var formData = {
            name: $("#name").val(),
            email: $("#email").val(),
            password: $("#password").val()
        };

        $.ajax({
            type: "POST",
            url: "/members/signup",
            contentType: "application/json",
            contentType : 'application/json; charset=utf-8',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(data) {
                alert('성공!');
                window.location.href = '/loginForm';
            },
            error: function(error) {
                alert("status : " + request.status + ", message : " + request.responseText + ", error : " + error);
            }
        });
    }
</script>
</body>
</html>
