<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false"%>
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
  <input type="text" name="email" value="${cookie.email.value}" placeholder="이메일 입력" autofocus>
  <input type="password" name="pwd" placeholder="비밀번호">
  <input type="hidden" name="toURL" value="${param.toURL}">
  <button>로그인</button>
  <div>
    <label><input type="checkbox" name="rememberEmail" value="on" ${empty cookie.email.value ? "":"checked"}> 아이디 기억</label> |
    <a href="">비밀번호 찾기</a> |
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

    function submitData() {
      var postData = {
        email: $("input[name='email']").val(),
        password: $("input[name='pwd']").val()
      };

      $.ajax({
        type: "POST",
        url: "/login", // 실제 로그인 엔드포인트로 업데이트
        contentType: "application/json;",
        data: JSON.stringify(postData),
        beforeSend: function (xhr) {
          xhr.setRequestHeader("${_csrf.headerName}", "${_csrf.token}");
        },
        success: function (response) {
          console.log("Success:", response);
        },
        error: function(error) {
          alert("status : " + request.status + ", message : " + request.responseText + ", error : " + error);
        }
      });
    }
  </script>


</form>
</body>
</html>