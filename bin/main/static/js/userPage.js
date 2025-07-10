const userPage = {
    init : function(){
        const _this = this;
        $('#post-list').on('change', function(){
            _this.selectPost($(this));
        });

        $('#image-post-list').on('change', function(){
            _this.selectImagePost($(this));
        })

        $('#comment-list').on('change', function(){
            _this.selectComment($(this));
        }); 
    },

    selectPost: function(select){
        const id = select.val();
        if(confirm("선택한 게시글을 확인할까요?")) {
            location.href = `/postCheck-page/${id}`;
        }
    },

    selectImagePost: function(select){
        const id = select.val();
        if(confirm("선택한 이미지를 확인할까요?")) {
            location.href = `/imagePostCheck-page/${id}`;
        }
    },   

    selectComment : function(select){
        const id = select.val();
        if(confirm("선택한 댓글의 게시글을 확인할까요?")) {
            location.href = `/postCheck-page/${id}`;
        }

    }
}

userPage.init();