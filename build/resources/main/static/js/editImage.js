const editImage = {
    init : function(){
        const _this = this;
        $('#btn-confirm-image').on('click', function(){
            _this.confirm($(this));
        })

        $('#btn-cancel-image').on('click', function(){
            _this.cancel();
        })
    },
    confirm : function(btn){
        const file = $('#image-upload-input')[0].files[0];
        const id = $('#user-id').val();

        if (!file || !file.type.startsWith('image/')) {
            alert('이미지 파일을 선택해주세요.');
            return;
        }

        const reader = new FileReader();
        reader.onload = function (e) {
        const base64 = e.target.result.split(',')[1]; // data:image/png;base64,XXX → base64 부분만 추출

        const data = {
            id : id,
            profileImageBase64 : base64
        };

        const url = `/api/imageUpload`;

        sendAjaxJson(btn, 'PATCH', url, data, function(result){
            window.opener.location.reload();
            window.close();
        })
    }
          reader.readAsDataURL(file);
    },

    cancel : function(){
        window.close();
    }
}

editImage.init();