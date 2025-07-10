const hedaer = {
    init : function(){
        const _this = this;
        $('#btn-search').on('click', function(){
            _this.search($(this));
        })
    },

    search : function(btn){
        const keyword = $('#search-keyword').val();
        if(keyword == null || keyword == "")
        {
            alert("키워드가 입력되지 않았습니다");
            return;
        }

        btn.prop('disabled', true);

        $.ajax({
        type : 'GET',
        url : `/search?keyword=${keyword}`,
        }).done(function(result){
            alert(result.message);

            if(result.success)
            {
                const keyword = result.keyword;
                location.href = `/postSearch-page?keyword=${keyword}`;
            }
        }).fail(function(error){
            alert(`서버 오류 발생 : ${error}`);
        }).always(function(){
            btn.prop('disabled', false);
        })
    }
}
hedaer.init();