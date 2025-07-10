const findPassword = {
    init : function(){
        const _this = this;
        $('#btn-check').on('click', function(){
            _this.check($(this));
        })
    },

    check : function(btn){
        const username = $('#username').val();
        const question = $('#question-select').val();
        const questionAnswer = $('#questionAnswer').val();

        const data ={
            username : username,
            question : question,
            questionAnswrd : questionAnswer
        };

        sendAjaxJson(btn, 'GET', '/api/user/findPassword', data, function(result){
            location.href = '/resetPassword-page';
        })
    }
}

findPassword.init();
