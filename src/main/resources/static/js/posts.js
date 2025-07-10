const posts = {
    init : function() {
        const _this = this;
        $('#search-btn').on('click', function() {
            _this.search();
        })
        $('#sort-view-btn').on('click', function(){
            _this.sort("viewCount");
        })
        $('#sort-date-btn').on('click', function(){
            _this.sort("createdDate");
        })
    },

    search : function() {
        const searchType = this.getSearchType();
        const searchKeyword = this.getSearchKeyword();

        if(searchKeyword == null || !searchKeyword.trim() || searchKeyword.length === 0)
        {
            alert("키워드를 입력해주세요");
            return;
        }

        location.href = `/posts-page?searchType=${searchType}&searchKeyword=${searchKeyword}`;
    },

    sort : function(type){
        const searchType = this.getSearchType();
        const searchKeyword = this.getSearchKeyword();

        location.href = `/posts-page?sortType=${type}&searchType=${searchType}&searchKeyword=${searchKeyword}`;
        desc = !desc;
    },

    getSearchType : function(){
        return $('#search-type').val();
    },

    getSearchKeyword : function(){
        return $('#search-keyword').val();
    }
}

posts.init();