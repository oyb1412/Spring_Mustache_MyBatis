let replyMode = false;
let parentCommentId = null;

const postCheck = {
 

    init : function() {
        const _this = this;

        $('#btn-post-delete').on('click', function() {
            _this.postDelete($(this));
        })

        $('#btn-comment-register').on('click', function() {
            _this.commentRegister($(this));
        })

        $('.btn-comment-delete').on('click', function() {
            _this.commentDelete($(this));
        })

        $('.btn-comment-reply').on('click', function() {
            _this.commentReply($(this));
        })

        $('#btn-like').on('click', function() {
            _this.like($(this));
        })
        
        $('#btn-dislike').on('click', function() {
            _this.dislike($(this));
        })
    },

    
//#region post
    postDelete : function($btn) {
        if(!confirm("정말 삭제하시겠습니까?")){
            return;
        }

        $btn.prop('disabled', true);

        const id = this.getPostId();

        $.ajax({
            type : 'DELETE',
            url : `/api/post/delete?id=${id}`,
        }).done(function(result){
            alert(result.message);

            if(result.success)
            {
                location.href = `/board-page/${$('#board-id').val()}`;
            }
        }).fail(function(error){
            alert(`서버 오류 발생 : ${error}`);
        }).always(function(){
            $btn.prop('disabled', false);
        });
    },
//#endregion
    
//#region comment
    commentRegister : function(btn) {
        const id = this.getPostId();
        const content = this.getCommentContent();

        if(!content.trim())
        {
            alert("내용을 입력해 주세요");
            return;
        }

        const commentData = {
            postId : id,
            parentCommentId : parentCommentId,
            content : content,
            type : "post"
        }

        sendAjaxJson(btn, 'POST', '/api/comment/register', commentData, function(result){
            replyMode = false;
            parentCommentId = null;
            //window.location.href = '/posts-page'
            //history.back();
            location.reload();
        });
    },

    commentDelete : function(btn){
        if(!confirm("정말 삭제하시겠습니까?")){
            return;
        }


        const id = btn.data('id');
        const postId = this.getPostId();

        const data = {
            id : id,
            postId : postId,
            type : "post"
        };

        sendAjaxJson(btn, 'DELETE', '/api/comment/delete', data, function(result){
            location.reload();
        });
    },

    commentReply : function(btn){
        replyMode = !replyMode;
        parentCommentId = btn.data('id');
        const replyText = $('#reply-mode-text');
        if(replyMode)
        {
            replyText.css('display', 'inline'); 
        }
        else
        {
            replyText.css('display', 'none');
        }
    },
//#endregion
    
//#region like
    like : function(btn){
        const id = this.getPostId();

        sendAjaxQuery(btn, 'PATCH', `/api/post/like?id=${id}`, function(result){
            location.reload();
        });
    },

    dislike : function(btn){
        const id = this.getPostId();

        sendAjaxQuery(btn, 'PATCH', `/api/post/dislike?id=${id}`, function(result){
            location.reload();
        });
    },
//#endregion

//#region other
    getPostId : function(){
        return $('#post-id').val();
    },

    getBoardId : function(){
        return $('#board-id').val();
    },

    getCommentContent : function(){
        return $('#comment-content').val();
    }
//#endregion
}

postCheck.init();