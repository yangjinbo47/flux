
function initWxJsConfig(lineLink, queryurl, imgUrl, descContent, shareTitle) {

	var success = {
		fn : function(data, status) {

			wx.config( {
				debug : false,
				appId : data.appid,
				timestamp : data.timestamp,
				nonceStr : data.nonceStr,
				signature : data.signature,
				jsApiList : [ 'chooseWXPay', 'openLocation', 'showAllNonBaseMenuItem', 'checkJsApi',
						'onMenuShareTimeline', 'onMenuShareAppMessage' ]
			});
			wx.ready(function() {
				
				var title = shareTitle.split('\^');
				if (title.length > 1) {
					var index = Math.ceil(Math.random() * title.length) - 1;
					shareTitle = title[index];
				}
				
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
	
	var param = {
		url : (location.href.split('#')[0]).replace(":80", "")
	};
	if (window.cpUser) {
		param.cpUser = window.cpUser;
	}
	ajaxRequest(queryurl, param, success)
}