const resetPassword = {
    init : function(){
        const _this = this;
        $('#btn-reset').on('click', function(){
            _this.reset($(this));
        })
    },

    reset : function(btn){
        const newPassword = $('#newPassword').val();
        const confirmPassword = $('#confirmPassword').val();

        const data ={
            newPassword : newPassword,
            confirmPassword : confirmPassword
        };

        sendAjaxJson(btn, 'PATCH', '/api/user/resetPassword', data, function(result){
            location.href = '/';
        })
    }
}

resetPassword.init();