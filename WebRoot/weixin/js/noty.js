/**
 * @Jquery提示插件。
 * @author galle.chu@skymobi.com
 * @version 1.0
 * @requires jQuery v1.2 or later
 * Created by Gallen Chu ( http://hi.baidu.com/zhuguoneng )
 */

/*
 * 2012-09-18: <version 1.0> - <galle.chu@skymobi.com> - <创建>
 */

;
(function($) {
	$.noty = function(options) {
		var defaults = {
			id: "noty_message",
			layout: 'topRight',
			type: '',
			text: '',
			animation: {
				open: {
					opacity: 'toggle',
					height: 'toggle'
				},
				close: {
					opacity: 'toggle',
					height: 'toggle'
				},
				easing: 'swing',
				speed: 500
			},
			timeout: 3000,
			parentDom: $(document.body),
			closeWith: ['click'],
			onShow: function() {},
			afterShow: function() {},
			onClose: function() {},
			afterClose: function() {}
		};
		var settings = $.extend({}, defaults, options),
			template = $('<div class="noty_message" id="' + settings.id + '" style="display:none;"><div class="noty_body"><span class="noty_text">' + settings.text + '</span></div></div>'),
			ie6 = !! window.ActiveXObject && !window.XMLHttpRequest;

		if ($("#" + settings.id).length > 0) {
			destroy();
		}

		function init() {
			template.find("div.noty_body").addClass(settings.type);
			settings.parentDom.append(template);
			setPos(template);
			onShow(template);

			if (settings.timeout >= 3000) {
				$.noty.prototype.timeoutFn = setTimeout(function() {
					onClose(template);
				}, settings.timeout);
			}

			if ($.inArray('hover', settings.closeWith) > -1) {
				template.one('mouseenter', function() {
					clearTimeout($.noty.prototype.timeoutFn);
					onClose(template);
				});
			}

			if ($.inArray('click', settings.closeWith) > -1) {
				template.one('click', function() {
					clearTimeout($.noty.prototype.timeoutFn);
					onClose(template);
				});
			}
		}

		function onShow(el) {
			if (settings.onShow) {
				settings.onShow();
			}
			el.animate(settings.animation.open, settings.animation.speed, settings.animation.easing, function() {
				if (settings.afterShow) {
					settings.afterShow();
				}
			});
			if (ie6) {
				$(window).bind("scroll", function() {
					setPos(template);
				});
			}
		}

		function onClose(el) {
			if (settings.onClose) {
				settings.onClose();
				if (settings.afterClose) {
					settings.afterClose();
				}
			}
			el.animate(settings.animation.close, settings.animation.speed, settings.animation.easing, function() {
				if (settings.afterClose) {
					settings.afterClose();
				}
				destroy();
			});
		}

		function destroy(){
			clearTimeout($.noty.prototype.timeoutFn);
			$("#noty_message").remove();
			if (ie6) {
				$(window).unbind("scroll");
			}
		}

		function setPos(el) {
			if (settings.layout == "topRight") {
				el.css({
					top: "30px",
					right: "30px"
				});
			} else if (settings.layout == "topLeft") {
				el.css({
					top: "30px",
					left: "30px"
				});
			} else if(settings.layout == "topCenter"){
				el.css({
					"top": "30px",
					"left": "50%",
					"margin-left": "-"+(el.width()/2)+"px"
				});
			} else if (settings.layout == "bottomRight") {
				el.css({
					bottom: "30px",
					right: "30px"
				});
			} else if (settings.layout == "bottomLeft") {
				el.css({
					bottom: "30px",
					left: "30px"
				});
			} else if (settings.layout == "center") {
				el.css({
					"top": "50%",
					"left": "50%",
					"margin-top": "-"+(el.height()/2)+"px",
					"margin-left": "-"+(el.width()/2)+"px"
				});
			} else if (settings.layout == "custom") {
				el.css({
					"top": "400px",
					"left": "476px"
				});
			}

			if (ie6) {
				if(settings.layout == "topRight" || settings.layout == "topLeft" || settings.layout == "topCenter" ){
					el.css({
						position: "absolute",
						top: parseInt($(document).scrollTop() + 30) + "px"
					});
				}else{
					el.css({
						position: "absolute",
						bottom: "40px"
					});
				}
			}
		}
		init();
	}
})(jQuery);