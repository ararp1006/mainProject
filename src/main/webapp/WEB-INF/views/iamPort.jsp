<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>결제</title>

    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa; /* Set your preferred background color */
            text-align: center;
            padding-top: 20px;
        }

        h1 {
            color: #007bff; /* Set your preferred color for the heading */
        }

        div {
            max-width: 400px;
            margin: 0 auto;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            background-color: #ffffff; /* Set your preferred background color */
        }

        h2 {
            color: #007bff; /* Set your preferred color for the subheading */
            margin-bottom: 20px;
        }

        button {
            background-color: #007bff; /* Set your preferred background color for the button */
            color: #fff; /* Set your preferred text color for the button */
            border: none;
            padding: 10px;
            border-radius: 4px;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3; /* Set your preferred background color for the button on hover */
        }

        script {
            margin-top: 20px;
        }
    </style>


</head>
<body>
<h1>결제창</h1>
<div>
    <script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js" ></script>
    <script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.1.5.js"></script>

    <div>
        <h2>결제하기</h2>

            <button id="iamportPayment" type="button">결제하기</button>

    </div>
</div>
<script>
 $(document).ready(function(){
     $("#iamportPayment").click(function(){
         payment();
     });
 })

    function payment(data){
        var IMP = window.IMP;
        IMP.init('imp27437633');

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
    }
</script>


</body>
</html>
