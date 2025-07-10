const postRegister ={
    init : function(){
        const _this = this;
        $('#btn-register').on('click', function() {
            _this.register($(this));
        });
    },

    register : function(btn) {
        const boardId = $('#boardId').val();
        const title = $('#title').val();
        let content = $('#summernote').val();
        const file = $('#upload-input')[0].files[0];

        if(!title.trim() || !content.trim())
        {
            alert("모든 칸을 채워주세요");
            return;
        }

        content = convertYoutubeLinks(content);

        const formData = new FormData();
        formData.append("boardId", boardId);
        formData.append("title", title);
        formData.append("content", content);

        if (file) {
            formData.append("file", file); 
        }

        sendAjaxFormdata(btn, 'POST', '/api/post/register', formData, function(result){
            window.location.href = `/board-page/${boardId}`;
        })
    },
}

postRegister.init();