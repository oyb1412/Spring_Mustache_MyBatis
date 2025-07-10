$(document).ready(function () {
  $('#summernote').summernote({
    placeholder: '여기에 내용을 입력하세요...',
    height: 300,
    lang: 'ko-KR', // 한글화 (원하면)
    toolbar: [
      ['style', ['bold', 'italic', 'underline', 'clear']],
      ['font', ['strikethrough', 'superscript', 'subscript']],
      ['fontsize', ['fontsize']],
      ['color', ['color']],
      ['para', ['ul', 'ol', 'paragraph']],
      ['insert', ['picture', 'link', 'video']],
      ['view', ['codeview']]
    ],
    callbacks: {
      onImageUpload: function(files) {
        const formData = new FormData();
        formData.append("file", files[0]);

        $.ajax({
          url: "/api/upload/image",
          method: "POST",
          data: formData,
          contentType: false,
          processData: false,
          success: function(url) {
            $('#summernote').summernote('insertImage', url);
          },
          error: function() {
            alert("이미지 업로드 실패");
          }
        });
      }
    }
  });
});