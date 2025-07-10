const userRegister = {
    init : function(){
        const _this = this;
        $('#btn-register').on('click', function() {
            _this.register($(this));
        });
    },

    register : function(btn){
        const username = $('#username').val();
        const password = $('#password').val();
        const checkPassword = $('#checkPassword').val();
        const question = $('#question-select').val();
        const questionAnswer = $('#questionAnswer').val();
        const nickname = $('#nickname').val();

        if(!username.trim() || !password.trim() || !checkPassword.trim() || !nickname.trim())
        {
            alert("모든 칸을 채워주세요");
            return;
        }
        
        const userData = {
            username : username,
            password : password,
            checkPassword : checkPassword,
            question : question,
            questionAnswer : questionAnswer,
            nickname : nickname
        };

        sendAjaxJson(btn, 'POST', '/api/user/register', userData, function(result){
            window.location.href = '/';
        })
    }
}

userRegister.init();