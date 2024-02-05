<%--
  Created by IntelliJ IDEA.
  User: aream
  Date: 2024-02-05
  Time: 오후 11:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js" ></script>
    <script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.1.5.js"></script>
    <!-- Add the following style block within the <head> tag of your HTML document -->
    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa; /* Set your preferred background color */
        }

        .card-body {
            max-width: 400px;
            margin: 50px auto;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            background-color: #ffffff; /* Set your preferred background color */
        }

        p {
            margin: 0 0 10px;
        }

        .box-radio-input {
            display: block;
            margin-bottom: 10px;
        }

        .box-radio-input input {
            display: none;
        }

        .box-radio-input span {
            display: inline-block;
            padding: 8px 12px;
            border: 1px solid #ccc;
            border-radius: 4px;
            cursor: pointer;
        }

        .box-radio-input input:checked + span {
            background-color: #007bff; /* Set your preferred background color for selected radio */
            color: #fff; /* Set your preferred text color for selected radio */
        }

        .btn-custom {
            background-color: #007bff; /* Set your preferred background color for the button */
            color: #fff; /* Set your preferred text color for the button */
            border: none;
            padding: 10px;
            border-radius: 4px;
            cursor: pointer;
        }

        .btn-custom:hover {
            background-color: #0056b3;
        }
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border: 1px solid transparent;
            border-radius: 4px;
        }

        .alert-success {
            background-color: #d4edda;
            border-color: #c3e6cb;
            color: #155724;
        }

        .alert-danger {
            background-color: #f8d7da;
            border-color: #f5c6cb;
            color: #721c24;
        }
    </style>



</head>
<body>
<h1>결제창</h1>
<div class="card-body bg-white mt-0 shadow">
    <p style="font-weight: bold">카카오페이 현재 사용가능</p>
    <label class="box-radio-input"><input type="radio" name="cp_item" value="5000"><span>5,000원</span></label>
    <label class="box-radio-input"><input type="radio" name="cp_item" value="10000"><span>10,000원</span></label>
    <label class="box-radio-input"><input type="radio" name="cp_item" value="15000"><span>15,000원</span></label>
    <label class="box-radio-input"><input type="radio" name="cp_item" value="20000"><span>20,000원</span></label>
    <label class="box-radio-input"><input type="radio" name="cp_item" value="25000"><span>25,000원</span></label>
    <label class="box-radio-input"><input type="radio" name="cp_item" value="30000"><span>30,000원</span></label>
    <label class="box-radio-input"><input type="radio" name="cp_item" value="35000"><span>35,000원</span></label>
    <label class="box-radio-input"><input type="radio" name="cp_item" value="40000"><span>40,000원</span></label>
    <label class="box-radio-input"><input type="radio" name="cp_item" value="50000"><span>50,000원</span></label>
    <p  style="color: #ac2925; margin-top: 30px">카카오페이의 최소 충전금액은 5,000원이며 <br/>최대 충전금액은 50,000원 입니다.</p>
    <button type="button" class="btn btn-lg btn-block  btn-custom" id="charge_kakao">충 전 하 기</button>
</div>
<script>
    var IMP = window.IMP; // 생략가능
    IMP.init('imp27437633'); // 'iamport' 대신 부여받은 "가맹점 식별코드"를 사용

    $('#charge_kakao').click(function () {
        // getter
        var IMP = window.IMP;
        IMP.init('imp27437633');
        var money = $('input[name="cp_item"]:checked').val();
        console.log(money);

        IMP.request_pay({
            pg: 'kakaopay.TC0ONETIME',
            merchant_uid: 'merchant_' + new Date().getTime(),
            pay_method: "card",
            name: '주문명 : 주문명 설정',
            amount: 17300,
            buyer_email: 'iamport@siot.do',
            buyer_name: '구매자이름',
            buyer_tel: '010-1234-5678',
            buyer_addr: '인천광역시 부평구',
            buyer_postcode: '123-456'
        }, function (rsp) {
            console.log(rsp);
            if (rsp.success) {
                alert("완료 -> imp_uid : " + rsp.imp_uid + "/merchant_uid(orderKdy) : " + rsp.merchant_uid);
            } else {
                var msg = '결제에 실패하였습니다.';
                msg += '에러내용 : ' + rsp.error_msg;
                alert(msg);
            }
        });
    });
</script>


</body>
</html>
