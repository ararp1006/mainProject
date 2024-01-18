
// AWS SDK 설정
AWS.config.region = 'ap-northeast-2';
AWS.config.credentials = new AWS.Credentials({
    accessKeyId: accessKey,
    secretAccessKey:  secretKey
});



var s3 = new AWS.S3({
    apiVersion: '2006-03-01',
    params: {Bucket: 'main-test-aream'} // S3 버킷 이름으로 변경
});

// S3에서 이미지 목록을 가져와서 화면에 표시하는 함수
function listImages() {
    var startIndex = (currentPage - 1) * pageSize;
    var endIndex = startIndex + pageSize;


    s3.listObjects({Delimiter: '/', Prefix: 'item/'}, function(err, data) {
        if (err) {
            return alert('Error listing images: ' + err.message);
        } else {
            var images = data.Contents.slice(startIndex, endIndex).map(function(image) {
                // 이미지 URL 생성
                var imageUrl = s3.getSignedUrl('getObject', {Bucket: 'main-test-aream', Key: image.Key});

                return '<div><a href="/detailPage.jsp?image=' + image.Key + '"><img src="' + imageUrl + '" style="width: 200px; height: 200px;"/></a><br/>'
                    + '<span>' + items+ '</span></div>';
            }).filter(Boolean);


            $('#imageContainer').html(images.join(''));

            // 페이지네이션 업데이트
            updatePagination(data.Contents.length);
        }
    });
}

// 페이지네이션 변수
var pageSize = 16; // 한 페이지에 표시되는 이미지 수
var currentPage = 1; // 현재 페이지

// 페이지네이션 업데이트 함수
function updatePagination(totalImages) {
    var totalPages = Math.ceil(totalImages / pageSize);

    // 페이지네이션 컨테이너 초기화
    $('#pageNation').empty();

    // 이전 페이지 버튼
    $('#pageNation').append('<button onclick="prevPage()">Previous</button>');

    // 각 페이지 버튼
    for (var i = 1; i <= totalPages; i++) {
        $('#pageNation').append('<button onclick="goToPage(' + i + ')">' + i + '</button>');
    }

    // 다음 페이지 버튼
    $('#pageNation').append('<button onclick="nextPage()">Next</button>');
}

// 이전 페이지로 이동 함수
function prevPage() {
    if (currentPage > 1) {
        currentPage--;
        listImages();
    }
}

// 다음 페이지로 이동 함수
function nextPage() {
    currentPage++;
    listImages();
}

// 특정 페이지로 이동 함수
function goToPage(page) {
    currentPage = page;
    listImages();
}

// 페이지 로드 시 이미지 목록을 가져옴
listImages();

