<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>🛒 TechComputeMall</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <style>
        #updateForm {
            max-width: 400px;
            margin: 0 auto;
        }

        /* 폼 레이블 스타일 */
        label {
            display: block;
            margin-top: 10px;
        }

        /* 입력 필드 스타일 */
        input {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
            box-sizing: border-box;
        }

        /* 파일 업로드 필드 스타일 */
        input[type="file"] {
            margin-top: 10px;
        }

        /* 버튼 스타일 */
        button {
            margin-top: 15px;
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        /* 버튼 호버 효과 */
        button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
<%@ include file="/WEB-INF/views/menu.jsp" %>
<div id="my-page-content">
    <h1>사용자 정보 수정</h1>

    <div id="updateForm">
        <label for="newName">새로운 이름:</label>
        <input type="text" id="newName" name="newName" placeholder="이름을 입력하세요">

        <label for="newEmail">새로운 이메일:</label>
        <input type="email" id="newEmail" name="newEmail" placeholder="이메일을 입력하세요">
        <label for="phoneNumber">전화번호:</label>
        <input type="tel" id="phoneNumber" name="phoneNumber" placeholder="숫자만 입력하세요">
        <label>주소</label>
        <div class="form-group">
            <input class="form-control" style="width: 40%; display: inline;" placeholder="우편번호" name="addr1" id="addr1" type="text" readonly="readonly">
            <button type="button" class="btn btn-default" onclick="execPostCode()"><i class="fa fa-search"></i> 우편번호 찾기</button>
        </div>
        <div class="form-group">
            <input class="form-control" style="top: 5px;" placeholder="도로명 주소" name="addr2" id="addr2" type="text" readonly="readonly"/>
        </div>
        <div class="form-group">
            <input class="form-control" placeholder="상세주소" name="addr3" id="addr3" type="text"/>
        </div>

        <form id="uploadForm" enctype="multipart/form-data">
            사진 <input type="file" id="file" name="file" multiple><br/>
        </form>

        <button type="button" onclick="submitUpdateUserInfo()">저장</button>
        <button type="button" onclick="cancelUpdateUserInfo()">취소</button>
    </div>
</div>

<script>
    function execPostCode() {
        new daum.Postcode({
            oncomplete: function (data) {
                var fullRoadAddr = data.roadAddress;
                var extraRoadAddr = '';

                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraRoadAddr += data.bname;
                }
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                if (extraRoadAddr !== '') {
                    extraRoadAddr = ' (' + extraRoadAddr + ')';
                }
                if (fullRoadAddr !== '') {
                    fullRoadAddr += extraRoadAddr;
                }

                console.log(data.zonecode);
                console.log(fullRoadAddr);

                $("[name=addr1]").val(data.zonecode);
                $("[name=addr2]").val(fullRoadAddr);
            }
        }).open();
    }
</script>
</body>
</html>
