//微信分享		
function initPage(lineLink, queryurl, imgUrl, descContent, shareTitle) {
	
	var imgUrl = checkString(imgUrl);
	if(imgUrl == null){
		imgUrl = "http://pdl.elevensky.net/www/TMP/mailiuliang/webrecharge.png";
	}
	var descContent = checkString(descContent);
	if(descContent == null){
		descContent = "魔品流量，有你想要的优惠";
	}
	var shareTitle = checkString(shareTitle);
	if(shareTitle == null){
		shareTitle = "春夏到秋冬，流量不停歇";
	}
	
	var success = {
		fn : function(data, status) {

			// data=eval('('+data+')');
			wx.config( {
				debug : false,
				appId : appid,
				timestamp : data.timestamp,
				nonceStr : data.nonceStr,
				signature : data.signature,
				jsApiList : [ 'chooseWXPay', 'openLocation', 'showAllNonBaseMenuItem', 'checkJsApi',
						'onMenuShareTimeline', 'onMenuShareAppMessage' ]
			});
			wx.ready(function() {

				// var imgUrl =
				// "http://pdl.elevensky.net/www/TMP/mailiuliang/webrecharge.png";
				// var lineLink =
				// "http://fm-client-web.mopote.com/fm-client-web-zxn/WX/index?cpUser=${cpUser}";
				// var lineLink = "${base}/share/index?cpUser=${cpUser}";
				// var descContent = "最多最全最便宜的流量加油包尽在魔品流量在线订购，小伙伴们还在等什么！";
				// var shareTitle = "三网最低价流量包尽在此";

				wx.onMenuShareAppMessage( {
					title : shareTitle, // 分享标题
					desc : descContent, // 分享描述
					link : lineLink, // 分享链接
					imgUrl : imgUrl, // 分享图标
					type : '', // 分享类型,music、video或link，不填默认为link
					dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
					success : function() {
						// 用户确认分享后执行的回调函数
					},
					cancel : function() {
						// 用户取消分享后执行的回调函数
					}
				});

				wx.onMenuShareTimeline( {
					title : shareTitle, // 分享标题
					link : lineLink, // 分享链接
					imgUrl : imgUrl, // 分享图标
					success : function() {
						// 用户确认分享后执行的回调函数
					},
					cancel : function() {
						// 用户取消分享后执行的回调函数
					}
				});

			});
		}
	};
	// var queryurl = "${base}/WX/getSign";
	var param = {
			url : (location.href.split('#')[0]).replace(":80", "")
	};
	if (window.cpUser) {
		param.cpUser = window.cpUser;
	}
	ajaxRequest(queryurl, param, success)
}

function checkString(param){
	if(param == null || param.length <=0 || param == "" || typeof(param) == 'undefined'){
		return null;
	}
	return param;
}
