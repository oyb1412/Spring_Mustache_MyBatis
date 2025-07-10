const loginUrlParams = new URLSearchParams(window.location.search);
if (loginUrlParams.get("error")) {
    alert(decodeURIComponent(loginUrlParams.get("message")));
}