const boardPage = {
    init : function(){
        const _this = this;
        $('#btn-post-write').on('click', function(){
            _this.write($(this));
        })
    },

    write : function(btn){
        const role = $('#role').val();
        const boardId = $('#board-id').val();
        if(role !== 'ADMIN' && boardId === 7)
        {
            alert("관리자만 공지사항을 작성할 수 있습니다");
            return;
        }

        location.href = `/postWrite-page/${boardId}`;
    }
}
boardPage.init();