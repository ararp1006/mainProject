// signUp.js

function checkEmail() {
    var emailToCheck = $("#email").val();

    $.ajax({
        type: "GET",
        url: "/members/checkEmail/" + emailToCheck,
        success: function(response) {
            if (response === true) {
                // 이미 존재하는 이메일인 경우
                alert("이미 사용 중인 이메일입니다. 다른 이메일을 입력해주세요.");
            } else {
                // 존재하지 않는 이메일인 경우, 회원가입 진행
                alert("사용 가능한 이메일입니다.");
            }
        },
        error: function(error) {
            alert("이메일 중복 확인 중 오류가 발생했습니다.");
        }
    });
}

function submitFormSignUp() {
    var formData = {
        name: $("#name").val(),
        email: $("#email").val(),
        password: $("#password").val()
    };

    console.log(JSON.stringify(formData));

    $.ajax({
        type: 'post',
        url: '/members/signup',
        data: JSON.stringify(formData),
        contentType : 'application/json; charset=utf-8',
    }).done(function(response){
        alert(response);
        window.location.href = '/loginForm';
    }). fail(function(error) {
    console.error("에러:", error);
    alert("에러: " + JSON.stringify(error));
});

}