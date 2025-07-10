const userModify = {
    init : function(){
        const _this = this;
        $('#btn-edit').on('click', function(){
            _this.modify($(this));
        })
        $('#btn-withdrawal').on('click', function(){
            _this.withdrawal($(this));
        })
    },

    modify : function(btn){
        if(!confirm("정말 수정하시겠습니까?"))
        {
            return;
        }

        const newPassword = $('#newPassword').val();
        const confirmPassword = $('#confirmPassword').val();
        const newNickname = $('#newNickname').val();

        const data = {
            newPassword : newPassword,
            confirmPassword : confirmPassword,
            newNickname : newNickname
        }

        sendAjaxJson(btn, 'PUT', '/api/user/modify', data, function(result){
            history.back();
        });
    },

    withdrawal : function(btn){
        if(!confirm("정말 탈퇴하시겠습니까?"))
        {
            return;
        }

        const id = $('#user-id').val();

        sendAjaxQuery(btn, 'DELETE', `/api/user/withdrawal?id=${id}`, function(result){
            location.href = "/userLogout";
        });

        
    }
}

userModify.init();