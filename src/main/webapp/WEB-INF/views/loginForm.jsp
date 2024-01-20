<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="loginId" value="${pageContext.request.getSession(false)==null ? '' : pageContext.request.session.getAttribute('id')}"/>
<c:set var="loginOutLink" value="${loginId=='' ? '/login/login' : '/login/logout'}"/>
<c:set var="loginOut" value="${loginId=='' ? 'Login' : 'ID='+=loginId}"/>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>loginForm</title>
  <link rel="stylesheet" href="<c:url value='/css/menu.css'/>">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css"/>
  <link rel="stylesheet" href="<c:url value='/css/loginForm.css'/>">
  <meta name="_csrf" content="${_csrf.token}"/>
  <meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>
<body>
<%@ include file="/WEB-INF/views/menu.jsp" %>
<form action="<c:url value="/login"/>" method="post" onsubmit="return formCheck(this);">
  <h3 id="title">Login</h3>
  <div id="msg">
    <c:if test="${not empty param.msg}">
      <i class="fa fa-exclamation-circle"> ${URLDecoder.decode(param.msg)}</i>
    </c:if>
  </div>
  <input type="text" name="email" id="email" value="${cookie.email.value}" placeholder="이메일 입력" autofocus>
  <input type="password" name="pwd" id="pwd" placeholder="비밀번호">

  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token }" />
  <input type="hidden" name="toURL" value="${param.toURL}">

  <button type="button" onclick="handleSubmit(event)">로그인</button>
  <div>
    <label><input type="checkbox" name="rememberEmail" value="on" ${empty cookie.email.value ? "":"checked"}> 아이디 기억</label> |
    <a href="">비밀번호 찾기</a>
    <a href="/signIn">회원가입</a>
  </div>
  <script>
    function formCheck(frm) {
      let msg = '';
      if (frm.email.value.length == 0) {
        setMessage('이메일을 입력해주세요.', frm.email);
        return false;
      }
      if (frm.pwd.value.length == 0) {
        setMessage('비밀번호를 입력해주세요.', frm.pwd);
        return false;
      }
      return true;
    }

    function setMessage(msg, element) {
      document.getElementById("msg").innerHTML = `<i class="fa fa-exclamation-circle"> ${msg}</i>`;
      if (element) {
        element.select();
      }
    }

    const URL = `${url}/login`;

    const handleSubmit = (e) => {
      e.preventDefault();
      const emailElement = document.getElementById('email');
      const passwordElement = document.getElementById('pwd');
      const email = emailElement ? emailElement.value : '';
      const password = passwordElement ? passwordElement.value : '';

      if (email.length && password.length) {
        const userInfo = {
          email,
          password,
        };

        const xhr = new XMLHttpRequest();
        xhr.open('POST', URL, true);
        xhr.setRequestHeader('Content-Type', 'application/json');

        xhr.onreadystatechange = function () {
          if (xhr.readyState === 4) {
            if (xhr.status === 200) {
              // Success
              const res = JSON.parse(xhr.responseText);
              console.log(res);

              const accessToken = xhr.getResponseHeader('authorization');
              const memberId = res.memberId;

              console.log(accessToken);
              localStorage.setItem('access_token', accessToken);
              localStorage.setItem('memberId', memberId);

              if (typeof setIsLogin === 'function') {
                setIsLogin(true);
              }
              window.location.href = "/home";
            } else {
              // Fail
              console.log(xhr.statusText);
            }
          }
        };

        xhr.send(JSON.stringify(userInfo));
      }
    };

  </script>


</form>
</body>
</html>