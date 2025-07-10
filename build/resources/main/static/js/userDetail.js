const adminPage = {
    init : function(){
        const _this = this;
        $('#btn-ban').on('click', function(){
            _this.ban($(this));
        });

        $('#btn-unban').on('click', function(){
            _this.unban($(this));
        })
    },

    ban : function(btn){
        if(!confirm("정말로 밴 하시겠습니까?")){
            return;
        }

        const id = this.getUserId();
        const url = `/api/user/ban?id=${id}`;
        sendAjaxQuery(btn, 'PATCH', url, function(result){
            location.reload();
        });
    },

    unban : function(btn){
        if(!confirm("정말로 밴 해제하시겠습니까?")){
            return;
        }

        const id = this.getUserId();
        const url = `/api/user/unban?id=${id}`;
        sendAjaxQuery(btn, 'PATCH', url, function(result){
            location.reload();
        });
    },

    getUserId : function(){
        return $('#user-id').val();
    }
}

adminPage.init();