<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>상품 등록하기</title>
</head>
<body>
<form id="uploadForm" enctype="multipart/form-data">

    파일: <input type="file" id="file" name="file" multiple><br/>
    상품명: <input type="text" id="name" required><br/>
    가격: <input type="number" id="price" min="100" required><br/>
    상세 설명: <textarea id="detail" required></textarea><br/>
    재고: <input type="text" id="status" required><br/>
    색상: <input type="text" id="color" required><br/>
    브랜드: <input type="text" id="brand" required><br/>
    카테고리: <input type="text" id="category" required><br/>
    <input type="submit" value="Upload">
</form>

<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script>
    $(document).ready(function () {
        $("#uploadForm").submit(function (event) {
            event.preventDefault();

            var formData = new FormData(); // FormData 객체 생성
            formData.append("file", $("#file")[0].files[0]); // 파일 추가

            var itemDto = {
                name: $("#name").val(),
                price: $("#price").val(),
                detail: $("#detail").val(),
                status: $("#status").val(),
                color: $("#color").val(),
                brand: $("#brand").val(),
                category: $("#category").val()
            };

            // itemDto 객체를 JSON 문자열로 변환하여 FormData에 추가
            formData.append("itemDto", new Blob([JSON.stringify(itemDto)], { type: "application/json" }));

            $.ajax({
                type: 'post',
                url: '/item/admin/items',
                data: formData,
                contentType: false,
                processData: false,
                success: function () {
                    window.location.reload();
                },
                error: function (error) {
                    alert(JSON.stringify(error));
                }
            });
        });
    });
</script>


</body>
</html>
