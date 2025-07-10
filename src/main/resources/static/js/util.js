function buttonOnEnable(btn, on)
{
    btn.prop('disabled', on);
}

function sendAjaxJson(btn, type, url,  data, successFunction)
{
    buttonOnEnable(btn, true);

    $.ajax({
        type : type,
        url : url,
        dataType : 'json',
        contentType : 'application/json; charset=utf-8',
        data : JSON.stringify(data)
    }).done(function(result){
            alert(result.message);

            if(result.success)
            {
                successFunction(result);
            }
        }).fail(function(error){
            alert(`서버 오류 발생 : ${error}`);
        }).always(function(){
            buttonOnEnable(btn, false);
        })
}

function sendAjaxQuery(btn, type, url, successFunction)
{
    buttonOnEnable(btn, true);

    $.ajax({
        type : type,
        url : url,
    }).done(function(result){
            alert(result.message);

            if(result.success)
            {
                successFunction(result);
            }
        }).fail(function(error){
            alert(`서버 오류 발생 : ${error}`);
        }).always(function(){
            buttonOnEnable(btn, false);
        })
}

function sendAjaxFormdata(btn, type, url, formData, successFunction)
{
    buttonOnEnable(btn, true);

    $.ajax({
            type : type,
            url : url,
            data : formData,
            contentType : false,
            processData : false,
        }).done(function(result){
            alert(result.message);

            if(result.success)
            {
                successFunction(result);
            }
        }).fail(function(error){
            alert(`서버 오류 발생 : ${error}`);
        }).always(function(){
            btn.prop('disabled', false);
        });
}

function convertYoutubeLinks(content) {
    const youtubeRegex = /https?:\/\/(?:www\.)?youtube\.com\/watch\?[^"'\s<>]+/g;

    return content.replace(youtubeRegex, function (url) {
        try {
            const urlObj = new URL(url);
            const videoId = urlObj.searchParams.get("v");

            if (!videoId) return url; // v 파라미터가 없으면 그대로 반환

            let embedUrl = `https://www.youtube.com/embed/${videoId}`;
            const params = [];

            // 필요한 추가 파라미터들 붙이기
            if (urlObj.searchParams.get("list")) {
                params.push(`list=${urlObj.searchParams.get("list")}`);
            }
            if (urlObj.searchParams.get("start_radio")) {
                params.push(`start_radio=${urlObj.searchParams.get("start_radio")}`);
            }

            if (params.length > 0) {
                embedUrl += `?${params.join("&")}`;
            }

            // & → &amp; 로 escape
            embedUrl = embedUrl.replace(/&/g, '&amp;');

            // iframe으로 감싸기
            return `<iframe width="560" height="315" src="${embedUrl}" frameborder="0" allowfullscreen></iframe>`;
        } catch (e) {
            // URL 파싱 실패 시 그냥 원본 URL 반환
            return url;
        }
    });
}
