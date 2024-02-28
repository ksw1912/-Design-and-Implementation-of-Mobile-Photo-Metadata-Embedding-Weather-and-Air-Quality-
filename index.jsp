<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Camera API</title>
       <script src="https://code.jquery.com/jquery-latest.min.js"></script>
       <!-- jQuery Mobile CSS -->
<link rel="stylesheet" href="https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css" />

<!-- jQuery Mobile JavaScript -->
<script src="https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
    </head>
 
    <body>
		 <form>	
		   <input type="file" name="uploadfile" multiple="multiple">
		 </form>
		  <hr />
 	 		<button id="uploadBtn">이미지 업로드</button>
			<div>
			  <a href='#' id='downloadBtn'>이미지 다운로드</a>
			</div>
			<img />
			<div>
			<button id="ExifView">exif 불러오기</button>
			</div>
			<div id="metadataDisplay"></div>
		 <script>
		let downloadpath;
		
		$("#uploadBtn").on("click", function(e) {
		    var formData = new FormData();
		    var inputFile = $("input[name='uploadfile']");
		    var files = inputFile[0].files;

		    // 선택된 파일들을 FormData에 추가
		    for (var i = 0; i < files.length; i++) {
		        formData.append("image", files[i]);
		    }

		    // Ajax를 통해 파일을 서버로 업로드
		    $.ajax({
		        url: '/Use_Ex/upload',
		        type: 'post',
		        data: formData,
		        processData: false, // 데이터 처리 방식을 설정하지 않음
		        contentType: false, // 컨텐츠 타입을 설정하지 않음
		        success: function(result) {
		            alert("Uploaded"+result);
		            downloadpath = result;
		        }
		    });
		});
			document.querySelector('#downloadBtn').addEventListener('click', () => {
				const imagepath =downloadpath;
				const requestData = {
			              	imagePath: imagepath
			            };
				fetch('/Use_Ex/download',{
						method: 'post',
		             	headers: {
		                'Content-Type': 'application/json'
		                },
		                body: JSON.stringify(requestData)
						})
				    .then(response => {
				      if (!response.ok) {
				        throw new Error('Network response was not ok');
				      }
				      return response.blob();
				    })
				    .then(blob => {
				      const url = window.URL.createObjectURL(blob);
				      const a = document.createElement('a');
				      a.style.display = 'none';
				      a.href = url;
				      a.download = 'downloaded_image.jpg'; //저장될 파일명
				      document.body.appendChild(a);
				      a.click();
				      window.URL.revokeObjectURL(url);
				    })
				    .catch(error => console.error('ERROR', error));
				});
			
			$("#ExifView").on("click", function(e) {
				getExif();
			});
			function getExif() {
				 const path = {path: downloadpath };
				    $.ajax({
				      type: "post",
				      url: "/Use_Ex/Exif",
				      accept: "application/json",
				      contentType: "application/json; charset=utf-8",
				      data: JSON.stringify(path),
				      dataType: "json",
				      success: function(data) {
				        ob = data;
				        console.log(ob);
				        displayMetadata(data); 
				     },
				     error: function(error) {
				         console.log("Error: ", error);
				     }
				});
			}
			function displayMetadata(metadata) {
			    var display = document.getElementById('metadataDisplay');
			    display.innerHTML = ''; // 기존 내용을 초기화

			    // 'Metadata' 키의 배열을 순회
			    metadata.Metadata.forEach(function(item) {
			        // 각 객체의 키-값 쌍을 순회하여 출력
			        for (var key in item) {
			            if (item.hasOwnProperty(key)) {
			                var value = item[key];
			                display.innerHTML += '<p><strong>' + key + ':</strong> ' + value + '</p>';
			            }
			        }
			    });
			}
    	</script>
    </body>
</html>