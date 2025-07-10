const postModify ={
    init : function() {
        const _this = this;
        $('#btn-modify').on('click', function() {
            _this.modify($(this));
            $('#btn-modify').prop('disabled', true);
        })
    },

    modify : function(btn) {
        const id = $('#post-id').val();
        const author = $('#post-author').val();
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
        formData.append("id", id);
        formData.append("title", title);
        formData.append("content", content);
        formData.append("author", author);


        if (file) {
            formData.append("file", file); 
        }

        const boardId = $('#board-id').val();

        sendAjaxFormdata(btn, 'PUT', '/api/post/modify', formData, function(result){
            window.location.href = `/board-page/${boardId}`;
        })
    }
}

postModify.init();