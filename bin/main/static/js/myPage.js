const myPage = {
    init : function(){
        const _this = this;
        $('#post-list').on('change', function(){
           _this.movePage($(this)); 
        });

        $('#btn-edit-image').on('click', function(){
            _this.editImage();
        })
    },

    movePage : function(select){
        const postId = select.val();
        location.href = `/post-page/${postId}`;
    },

    editImage : function(){
        const userId = $('#user-id').val();
        const url = `/edit-image-page?id=${userId}`;

        window.open(url, '프로필 이미지 수정', 'width=400,height=500,scrollbars=no');
    }
}

myPage.init();