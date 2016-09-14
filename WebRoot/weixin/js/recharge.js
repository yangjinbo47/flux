(function($) {
	var regx = "^(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$";
	var re = "^[0-9]+$";
	var _pckMsg = $("#package-message"), _package = $("#package"), _btnRecharge = $("#btnConfirmRecharge");
	var _msgName = $("#recharge-message_1");
	var openId = $("#openId").val();
	var mbUsableMB = $("#mbUsable").html();
	var provPackage = false;

	var _pageLoginNum = $("#pageLoginNum").val();
	var _selectCoupons = $("#couponsId").val();
	var _area, _bizs_flow, _bizs_prov, _bizs_quanguo;
	var _number, _selected, _selected_phoneValue;

	var rechargeing = false;

	var isShowFlag = false;

	var numberChange = function() {
		var number = $("#inputCellPhoneNumber").val();
		var isNum = new RegExp(re).test(number);
		var flag = new RegExp(regx).test(number);
		if (!isNum) {
			$("#inputCellPhoneNumber").val("");
			intInputNumber();
			updateBtnRechargeStatus();
			return;
		}

		if (number == _number)
			return;

		if (number != null && number != '' && flag) {
			getBizs(number);
			$("#yyImage").addClass('hide');
			$("#cl1").addClass("hide");
			$("#cl2").removeClass("hide");
			if (!$(".recharge-help").hasClass('hide')) {
				$(".recharge-help").addClass('hide');
			}

			if ($("#nav_b").hasClass('hide')) {
				$("#nav_b").removeClass('hide');
			}

			$("#recharge-message_1").html("");
			$("#recharge-message_1").removeClass("hide");
			_number = number;
		} else {
			_number = null;
			_selected = null;
			_selected_phoneValue = null;
			$("#yyImage").removeClass('hide');
			$(".modPackage").addClass("hide");
			$(".provPackage").addClass("hide");
			$("#cl2").addClass("hide");
			$("#dginfo").addClass("hide");
			$("#tabDiv").removeClass("hide");

			$(".tip").html("<p>输入要充值的号码，系统会自动适配流量包</p>");
			if (!$(".recharge-footer").hasClass('hide')) {
				$(".recharge-footer").addClass('hide');
			}

			$(".tip").show();
			$("#cl1").removeClass("hide");
			$("#recharge-message_1").addClass("hide");

			_package.empty();
			$(".mOne").each(function() {
				$(this).remove();
			});
			var ali = $('<li class="mOne"></li>');
			$(".normalPackage").append(ali);
			$(".mTwo").each(function() {
						$(this).remove();
					});
			var bli = $('<li class="mTwo"></li>');
			$(".pPackage").append(bli);
			$("#amount").text(0);
			$("#present s").html('');
			updateBtnRechargeStatus();
			if ((number || "").length == 11) {
				$(".tip p").html("抱歉，您所输入的号码无法充值，请查看订购须知");
				$(".tip p").addClass('tipColor');
			}
			return;
		}
	}
	var internal = {
		init : function() {
			$("#inputCellPhoneNumber").focus();
			$('#inputCellPhoneNumber').bind('input propertychange',
					numberChange);
			if (_pageLoginNum != null && _pageLoginNum != '') {
				$('#inputCellPhoneNumber').val(_pageLoginNum);
				numberChange();
			} else {
				$('#inputCellPhoneNumber').val('');
			}
		}
	};
	var success = {
		fn : function(json) {
			//			_bizs_tejia = null;
			_bizs_quanguo = null;
			_bizs_prov = null;
			//			_bizs_flow = null;
			var Flag = json.Flag;
			if (Flag != null && Flag == 0) {
				var ps = json.Ps;
				var provPs = json.provPs;
				_area = json.Data.region;
				_bizs_prov = json.Data.provResource;//省包
				_bizs_quanguo = json.Data.quanguoResource;//全国包
				//				_tejiabao = json.Data.tejiabao;
				_msgName.html(_area.provinceName + _area.opName);
				$(".jiayoubao .fl i").text('     ' + ps);
				$(".prov .fl i").text('     ' + provPs);
			} else {
				_area = null;
				_selected = null;
				_selected_phoneValue = null;
				$('#amount').text('0');
				updateBtnRechargeStatus();
				if (json.Flag == -10) { // 手机号码空
					$('.tip p').html(json.Message);
					$(".tip p").addClass('tipColor');
				} else if (json.Flag == -11 || json.Flag == -99) { // 不支持
					_pckMsg.removeClass('hide');
					$('.tip p').html(json.Message);
					$(".tip p").addClass('tipColor');
				} else if (json.Flag == -12) { // 没有可充值的包
					_pckMsg.removeClass("hide");
					$('.tip').html(json.Message);
				} else if (json.Flag == 12) {
					_area = json.Data.region;
					_pckMsg.removeClass("hide");

					$('.tip p').html("输入的手机号码当前没有可充值的流量包");
					$(".tip p").addClass('tipColor');
				} else {
					swal(json.Message);
				}
			}
			renderBizList();
			if (_area) {
				var cls = 'cmcc';
				if (_area.opCode == 1)
					cls = 'cmcc';
				else if (_area.opCode == 2)
					cls = 'cucc';
				else if (_area.opCode == 3)
					cls = 'ctcc';
			}
		}
	};

	var paysuccess = {
		fn : function(json) {
			var Flag = json.Flag;
			if (Flag != null && Flag == 0) {
				$("#successOrder").val(json.Data.successOrder);
				$("#resourceType").val(json.Data.resourceType);
				var option = {
					appid : json.Data.appid,
					timeStamp : json.Data.payTimestamp,
					nonceStr : json.Data.payNonceStr,
					packageValue : 'prepay_id=' + json.Data.prePayId,
					paySign : json.Data.paySign
				};
				_btnRecharge.text(_btnRecharge.data("reset"));

				setTimeout(function() {
							$("#messager").empty();
							_btnRecharge.prop("disabled", false);
						}, 3000);
				pay(option);
			} else {
				swal(json.Message)
			}
			// 订单创建完成，调用充值
			// recharge();
			rechargeing = false;
			_btnRecharge.text(_btnRecharge.data("reset"));

			setTimeout(function() {
						$("#messager").empty();
						_btnRecharge.prop("disabled", false);
					}, 5000);
		}
	};

	function getBizs(number) {
		$("#messager").empty();
				ajaxRequest("../internal/business_getPackage.action", {
//		ajaxRequest("getPackage.json", {
					cellPhoneNumber : number
				}, success, timeoutFunction);
	}

	var preyPayTimeoutFunction = {
		fn : function() {

			swal("服务器连接错误，请检查网络重试！");
			rechargeing = false;
			_btnRecharge.text(_btnRecharge.data("reset"));
			$("#messager").empty();
			_btnRecharge.prop("disabled", false);
		}
	};
	function WXpayMoney() {
		// 微信支付，支付成功将在market表中创建
		rechargeing = true;
		// Modal.close();
		_btnRecharge.data("reset", _btnRecharge.text()).text(_btnRecharge
						.data("loading")).prop("disabled", true);
		// 以下代表支付成功，生成订单
		ajaxRequest("../internal/wxpay_prepay.action", {
					flow : _selected.amount,
					range : _selected.range,
					packageId : _selected.packageId,
					productId : _selected.productId,
					phoneNumber : _number,
//					name : encodeURI(_selected.name),
					name : _selected.name,
					price : _selected.marketPrice,
					salePrice : _selected.salePrice,
					businessId : _selected.id,
					phoneValue : _selected_phoneValue,
					openId : openId
				}, paysuccess, preyPayTimeoutFunction);

	}

	function renderBizList() {
		_pckMsg.addClass("hide");
		_package.empty();
		_selected = null;
		_selected_phoneValue = null;
		$('#amount').text('0.00');
		$("#present s").text();
		updateBtnRechargeStatus();
		var have_quanguo = _bizs_quanguo == null || _bizs_quanguo.length == 0;
		var have_prov = _bizs_prov == null|| _bizs_prov.length == 0;
		if (have_quanguo && have_prov) {
			_number = null;
			_selected = null;
			_selected_phoneValue = null;
			if(_bizs_quanguo == null || _bizs_quanguo.length == 0){
				$(".modPackage").addClass("hide");
				$("#cl2").addClass("hide");
			}
			if(_bizs_prov == null|| _bizs_prov.length == 0){				
				$(".provPackage").addClass("hide");
				$("#cl3").addClass("hide");
			}
			

			if (!$("#btn-footer").hasClass('hide')) {
				$("#btn-footer").addClass("hide");
			}

			if (!$("#dginfo").hasClass('hide')) {
				$("#dginfo").addClass("hide");
			}
			$("#tabDiv").removeClass("hide");
			if (!$(".recharge-info").hasClass('hide')) {
				$(".recharge-info").addClass('hide');
			}

			$(".tip").show();
			$("#cl1").removeClass("hide");
			_package.empty();
			$(".mOne").each(function() {
						$(this).remove();
					});

			if ($("#tabDiv").hasClass('hide')) {
				$("#tabDiv").removeClass("hide");
			}

			$(".tip").removeClass('hide');
			$(".tip p").html("因运营商维护，当前无可用流量包，请稍后再试");
			$(".tip p").addClass('tipColor');
			return;
		} else {
			if(!have_quanguo){
				if ($(".modPackage").hasClass('hide')) {
					$(".modPackage").removeClass("hide");
				}
			}
			if(!have_prov){
				if ($(".provPackage").hasClass('hide')) {
					$(".provPackage").removeClass("hide");
				}
			}
			if (!$("#tabDiv").hasClass('hide')) {
				$("#tabDiv").addClass("hide");
			}
			$(".tip").hide();

			if ($("#dginfo").hasClass('hide')) {
				$("#dginfo").removeClass('hide');
			}
			$(".recharge-footer").removeClass("hide");
		}

		//		var bizs_tejia = _bizs_tejia;
		var bizs_quanguo = _bizs_quanguo;
		var bizs_prov = _bizs_prov;
		//		var bizs_flow = _bizs_flow;
		//		var tejiabaos = _tejiabao;
		var i = 0;
		var first = 0;
		var j = 0;
		var f = 0;
		//			$(tejiabaos).each(
		//function(j,tejiabao){
		//var name_tejia = 0;//包名
		//var main_amount = 0;//主类大小
		//var vice_amount = 0;//话费面额;先不写返麦b
		//var unit = "M";
		//if(tejiabao.resource.amount >= 1048576){
		//    main_amount = (parseInt(tejiabao.resource.amount)/1024/1024).toFixed(1);
		//    unit = "G";
		//           }else{
		//                   main_amount = (parseInt(tejiabao.resource.amount)/1024).toFixed(0);
		//                   }
		//			
		//                  var mbRebateAmount = 0;
		//                  var li = null;
		//                  if($(tejiabao.resource.present).size()==0){
		//                	  li = $('<div class="ml"><i>'+tejiabao.markContent+'</i><h3 class="mCtwo">'
		//	                		   +main_amount+unit+'</br>' +tejiabao.phoneValue + '元话费</h3><img src="images/image_tejia_choice.png" alt=""></div>');  
		//                	  
		//                  }else{
		//                  $(tejiabao.resource.present).each(function(j, pst) {                	  
		//                	  li = $('<div class="ml"><i>'+tejiabao.markContent+'</i><h3 class="mCtwo">'
		//            		   +main_amount+unit+'</br>' +tejiabao.phoneValue + '元话费</h3><img src="images/image_tejia_choice.png" alt=""></div>');  
		//                	  
		//              	});}
		//             _tejiaResource.append(li);
		//                           if (j % 3 == 1) {
		//                                li.addClass('mC');
		//                               var a=$(".PackageCont").width();
		//                               var b=(a-93)/2;
		//                               $(".ml.mC").css("left",b);
		//                            }
		//                            if (j% 3 == 2) {
		//                                first = j;
		//                            }
		//                            						
		//                            var range = ((tejiabao.resource.range == 0) ? '全国' : '省内');
		//                            tejiabao.resource.amountCount = main_amount + unit;
		//                            li.data("data", tejiabao.resource);
		//                            li.click(function() {
		//                            	if($(".Slt").hasClass('Slt1') || $(".Slt").hasClass('Slt2') || $(".Slt").hasClass('Slt3'))
		//                					$(".Slt").removeClass('Slt1').removeClass('Slt2').removeClass('Slt3');
		//                            	
		//                            	 $(this).addClass("Slt3");
		//                            	
		//                            	
		//                            	 $(".Slt").children('img').hide();
		//                                 $(".Slt").removeClass("Slt");
		//                                 $(this).addClass("Slt");
		//                                 $(this).children('img').show();  
		//                                 var b = $(this).data("data");
		// 								_selected = b;
		// 								_selected_phoneValue = tejiabao.phoneValue;
		// 								var isLow = false;
		// 								if(_selected.salePrice/_selected.marketPrice*100<_resconf.minDiscount){
		// 									isLow = true;
		// 								}
		// 								if(isLow) {
		// 									if(!$("#nav_a").hasClass('hide')){
		//	                            		$("#nav_a").addClass('hide');
		//	                            	}
		//	                            	if(!$(".notTejia").hasClass('hide')){
		//	                            		$(".notTejia").addClass('hide');
		//	                            	}
		// 								} else {
		//	                            	if($("#nav_a").hasClass('hide')){
		//	                            		$("#nav_a").removeClass('hide');
		//	                            	}
		//	                            	if(!$(".notTejia").hasClass('hide')){
		//	                            		$(".notTejia").addClass('hide');
		//	                            	}
		// 								}
		// 								isTejia = true;
		// 								var effectiveDateName='';
		//								if(b.effectiveDate==1){
		//									effectiveDateName='立即生效';
		//								}else if(b.effectiveDate==2){
		//									effectiveDateName='次月生效';
		//								}else if(b.effectiveDate==3){
		//									effectiveDateName='24小时内后生效';
		//								}else if(b.effectiveDate==4){
		//									effectiveDateName='当天生效';
		//								}else if(b.effectiveDate==5){
		//									effectiveDateName='当月生效';
		//								}
		//								$(".cl2 span i").text(''+effectiveDateName);
		//								
		//								//ajax请求获取最优抵价券--cpuser为1才能用
		//								if(FCcpUser==1&&(!$("#couponsDiv").hasClass("hide"))&&(v=='0')){
		//								getBestCoupons();
		//								}
		//								$("#present s").text("￥"+(parseInt(b.marketPrice) / 100)
		//										.toFixed(2));
		//								$("#amount").text(
		//										(parseInt(b.salePrice) / 100)
		//												.toFixed(2));
		//								
		//								if((parseInt(b.salePrice)) >= (parseInt(b.marketPrice))){
		//									$("#present s").html("");
		//								}
		//								var mbRebateAmount = 0;
		//								 if(!$("#present s").hasClass('back')){
		//									   $("#present s").addClass('back');
		//									   if(!$("#present p").hasClass('hide')){
		//										   $("#present p").addClass('hide');
		//									   }
		//								   }
		//								 
		//								
		//								
		//								$('#fanMB').html(mbRebateAmount);
		//								$("#MB").removeClass('hide');
		//								$("#useMB").val("");
		//								$("#rmb").html("0.00");
		//								////
		//								
		//								if (!$("#MB").hasClass('hide')) {
		//									$("#MB").addClass('hide');
		//								}
		//								if ($("#MB").hasClass('checked')) {
		//									$("#MB").removeClass('checked').addClass(
		//									'unchecked');
		//									$("#mbtip").addClass('hide');	
		//								}
		//								if (!$("#userMBdiv").hasClass('hide')) {
		//									$("#userMBdiv").addClass('hide');
		//								}
		//								
		//								
		//								if ($("#coupons").hasClass('checked')) {
		//									$("#coupons").removeClass('checked').addClass(
		//									'unchecked');
		//									$(".couponTip").addClass('hide');
		//								}
		//								
		//								if (!$("#naming").hasClass('hide')) {
		//									$("#naming").addClass('hide');
		//								}
		//								////
		//								updateBtnRechargeStatus();
		//								unbindMBClick();
		//								bindMBClick();
		//							});
		//                            j++;
		//                           }
		//                        );		

		//    $(bizs_flow).each(
		//    		function(f,flowRes){
		//    			var name_flow = 0;
		//    			var main_amount = 0;
		//    			var vice_amount = 0;
		//    			var unit = "M";
		//    			if(flowRes.mainAmount >= 1048576){
		//    				main_amount =(parseInt(flowRes.mainAmount)/1024/1024).toFixed(1);
		//    				unit = "G";
		//    			}else{
		//    				main_amount = (parseInt(flowRes.mainAmount)/1024).toFixed(0);
		//    			}
		//    			
		//    			var mbRebateAmount = 0;
		//    			var li = null;
		//    			li = $('<div class="ml"><i>' + flowRes.markContent + '</i><h3 >'
		//    			+main_amount+unit+'</br></h3><img src="images/image_jiayou_choice.png" alt=""></div>');
		//    			$(".mOne").last().append(li);
		//    			if(f%3==1){
		//    				li.addClass('mC');
		//    				var a = $(".normalPackage").width();
		//    				var b = (a-93)/2;
		//    				$(".ml.mC").css("left",b);
		//    			}
		//    			if(f%3 ==2){
		//    				first = f;
		//    			}
		//    			
		//    			var range = ((flowRes.resource.range ==0)?'全国':'省内');
		////    			flowRes.resource.amountCount = main_amount + unit;
		//    			li.data("data",flowRes.resource);
		//    			li.click(function(){    				
		//    				if($(".Slt").hasClass('Slt1') || $(".Slt").hasClass('Slt2') || $(".Slt").hasClass('Slt3'))
		//    					$(".Slt").removeClass('Slt1').removeClass('Slt2').removeClass('Slt3');
		//    				
		//    				$(this).addClass("Slt1");
		//    				$(".Slt").children('img').hide();
		//    				$(".Slt").removeClass("Slt");
		//    				$(this).addClass("Slt");
		//    				$(this).children('img').show();
		//    				var b = $(this).data("data");
		//    				_selected = b;
		//					var isLow = false;
		//					if(_selected.salePrice/_selected.marketPrice*100<_resconf.minDiscount){
		//						isLow = true;
		//					}
		//					if(isLow) {
		//						if(!$("#nav_a").hasClass('hide')){
		//	    					$("#nav_a").addClass('hide');
		//	    				}
		//	    				if(!$('.notTejia').hasClass('hide')){
		//	    					$(".notTejia").addClass('hide');
		//	    				}
		//					}else{
		//	    				if($("#nav_a").hasClass('hide')){
		//	    					$("#nav_a").removeClass('hide');
		//	    				}
		//	    				if(!$('.notTejia').hasClass('hide')){
		//	    					$(".notTejia").addClass('hide');
		//	    				}
		//					}
		//    				var msgInfo = $(".name").html();
		//    				var yys = msgInfo.substring(msgInfo.length-2,msgInfo.length);
		//    				_selected.name = range+yys+main_amount+unit;
		//    				_selected_phoneValue=0;
		//    				isTejia = true;
		//    				var effectiveDateName='';
		//    				if(b.effectiveDate==1){
		//						effectiveDateName='立即生效';
		//					}else if(b.effectiveDate==2){
		//						effectiveDateName='次月生效';
		//					}else if(b.effectiveDate==3){
		//						effectiveDateName='24小时内后生效';
		//					}else if(b.effectiveDate==4){
		//						effectiveDateName='当天生效';
		//					}else if(b.effectiveDate==5){
		//						effectiveDateName='当月生效';
		//					}
		//					$(".cl2 span i").text(''+effectiveDateName);
		//					
		//					if(FCcpUser==1&&(!$("#couponsDiv").hasClass("hide"))&&(v=='0')){
		//						getBestCoupons();
		//						}
		//						$("#present s").text("￥"+(parseInt(b.marketPrice) / 100)
		//								.toFixed(2));
		//						$("#amount").text(
		//								(parseInt(b.salePrice) / 100)
		//										.toFixed(2));
		//						if((parseInt(b.salePrice)) >= (parseInt(b.marketPrice))){
		//							$("#present s").html("");
		//						}
		//						var mbRebateAmount = 0;
		//						 if(!$("#present s").hasClass('back')){
		//							   $("#present s").addClass('back');
		//							   if(!$("#present p").hasClass('hide')){
		//								   $("#present p").addClass('hide');
		//							   }
		//						   }
		//						 
		//						 $('#fanMB').html(mbRebateAmount);
		//							$("#MB").removeClass('hide');
		//							$("#useMB").val("");
		//							$("#rmb").html("0.00");
		//							////
		//							
		//							if (!$("#MB").hasClass('hide')) {
		//								$("#MB").addClass('hide');
		//							}
		//							if ($("#MB").hasClass('checked')) {
		//								$("#MB").removeClass('checked').addClass(
		//								'unchecked');
		//								$("#mbtip").addClass('hide');	
		//							}
		//							if (!$("#userMBdiv").hasClass('hide')) {
		//								$("#userMBdiv").addClass('hide');
		//							}
		//							
		//							
		//							if ($("#coupons").hasClass('checked')) {
		//								$("#coupons").removeClass('checked').addClass(
		//								'unchecked');
		//								$(".couponTip").addClass('hide');
		//							}
		//							
		//							if (!$("#naming").hasClass('hide')) {
		//								$("#naming").addClass('hide');
		//							}
		//							////
		//							updateBtnRechargeStatus();
		//							unbindMBClick();
		//							bindMBClick();
		//    			}
		//    			);
		//    			f++;
		//    		}
		//    );
		$(bizs_quanguo).each(function(i, biz) {
			var amount = 0;
			var unit = "M";
			if (biz.amount >= 1024) {
				amount = (parseInt(biz.amount) / 1024).toFixed(1);
				unit = "G";
			} else {
				amount = (parseInt(biz.amount)).toFixed(0);
			}
			var li;

			var isLow = false;
			if (biz.salePrice < biz.marketPrice) {
				isLow = true;
			}
			if (i % 3 == 0) {
				li = $('<div class="ml mNone"><h3>'
						+ amount
						+ unit
						+ '<br></h3><img src="images/image_jiayou_choice.png" alt=""></div>');
				$(".mOne").last().append(li);
			}
			if (i % 3 == 1) {
				li = $('<div class="ml mNone"><h3>'
						+ amount
						+ unit
						+ '</h3><img src="images/image_jiayou_choice.png" alt=""></div>');
				$(".mOne").last().append(li);
			}
			if (i % 3 == 2) {
				li = $('<div class="ml mNone"><h3>'
						+ amount
						+ unit
						+ '</h3><img src="images/image_jiayou_choice.png" alt=""></div>');

				$(".mOne").last().append(li);
				if (i < ($(bizs_quanguo).length - 1)) {
					$(".mOne").last().after("<li class='mOne'></li>");
				}
			}
			if ((i / 3) > 2) {
				$(".mOne").addClass('hide');
				$(".normalPackage").find('li:first').removeClass('hide');
				$(".normalPackage li").eq(1).removeClass('hide');
			}
			if (i % 3 == 1) {
				li.addClass('mC');
				var a = $(".normalPackage").width();
				var b = (a - 93) / 2;
				$(".ml.mC").css("left", b);
			}
			if (i > 5) {
				$(".mOne").addClass('hide');
				$(".normalPackage").find('li:first').removeClass('hide');
				$(".normalPackage li").eq(1).removeClass('hide');
			}

			biz.amountCount = amount + unit;//biz.amountCount = amount + unit;
			li.data("data", biz);
			li.click(function() {
				if ($(".ml").hasClass('Slt1')) {
					$(".ml").removeClass('Slt1');
				}

				if ($(".ml").hasClass('Slt2')) {
					$(".ml").removeClass('Slt2');
				}

				if ($(".ml").hasClass('Slt3')) {
					$(".ml").removeClass('Slt3');
				}

				$(this).addClass("Slt2");

				$(".Slt").children('img').hide();
				$(".Slt").removeClass("Slt");
				$(this).addClass("Slt");
				$(this).children('img').show();
				var b = $(this).data("data");
				_selected = b;
				var isLow = false;
				if (_selected.salePrice < _selected.marketPrice) {
					isLow = true;
				}
				_selected_phoneValue = 0;

				$("#present s").text("￥"
						+ (parseInt(b.marketPrice) / 100).toFixed(2));
				$("#amount").text((parseInt(b.salePrice) / 100).toFixed(2));
				if ((parseInt(b.salePrice)) >= (parseInt(b.marketPrice))) {
					$("#present s").text('');
				}
				$("#present p").removeClass('hide');
				$("#present s").removeClass('back');

				if (isLow) {
					$("#present p").addClass('hide');
					$("#present s").addClass('back');
				}

				updateBtnRechargeStatus();
			});

			i++;
		});
		$(bizs_prov).each(function(i, biz) {
			var amount = 0;
			var unit = "M";
			if (biz.amount >= 1024) {
				amount = (parseInt(biz.amount) / 1024).toFixed(1);
				unit = "G";
			} else {
				amount = (parseInt(biz.amount)).toFixed(0);
			}
			var li;

			var isLow = false;
			if (biz.salePrice < biz.marketPrice) {
				isLow = true;
			}
			if (i % 3 == 0) {
				li = $('<div class="ml mNone"><h3>'
						+ amount
						+ unit
						+ '<br></h3><img src="images/image_jiayou_choice.png" alt=""></div>');
				$(".mTwo").last().append(li);
			}
			if (i % 3 == 1) {
				li = $('<div class="ml mNone"><h3>'
						+ amount
						+ unit
						+ '</h3><img src="images/image_jiayou_choice.png" alt=""></div>');
				$(".mTwo").last().append(li);
			}
			if (i % 3 == 2) {
				li = $('<div class="ml mNone"><h3>'
						+ amount
						+ unit
						+ '</h3><img src="images/image_jiayou_choice.png" alt=""></div>');

				$(".mTwo").last().append(li);
				if (i < ($(bizs_prov).length - 1)) {
					$(".mTwo").last().after("<li class='mTwo'></li>");
				}
			}
			if ((i / 3) > 2) {
				$(".mTwo").addClass('hide');
				$(".pPackage").find('li:first').removeClass('hide');
				$(".pPackage li").eq(1).removeClass('hide');
			}
			if (i % 3 == 1) {
				li.addClass('mC');
				var a = $(".pPackage").width();
				var b = (a - 93) / 2;
				$(".ml.mC").css("left", b);
			}
			if (i > 5) {
				$(".mTwo").addClass('hide');
				$(".pPackage").find('li:first').removeClass('hide');
				$(".pPackage li").eq(1).removeClass('hide');
			}

			biz.amountCount = amount + unit;//biz.amountCount = amount + unit;
			li.data("data", biz);
			li.click(function() {
				if ($(".ml").hasClass('Slt1')) {
					$(".ml").removeClass('Slt1');
				}

				if ($(".ml").hasClass('Slt2')) {
					$(".ml").removeClass('Slt2');
				}

				if ($(".ml").hasClass('Slt3')) {
					$(".ml").removeClass('Slt3');
				}

				$(this).addClass("Slt2");

				$(".Slt").children('img').hide();
				$(".Slt").removeClass("Slt");
				$(this).addClass("Slt");
				$(this).children('img').show();
				var b = $(this).data("data");
				_selected = b;
				var isLow = false;
				if (_selected.salePrice < _selected.marketPrice) {
					isLow = true;
				}
				_selected_phoneValue = 0;

				$("#present s").text("￥"
						+ (parseInt(b.marketPrice) / 100).toFixed(2));
				$("#amount").text((parseInt(b.salePrice) / 100).toFixed(2));
				if ((parseInt(b.salePrice)) >= (parseInt(b.marketPrice))) {
					$("#present s").text('');
				}
				$("#present p").removeClass('hide');
				$("#present s").removeClass('back');

				if (isLow) {
					$("#present p").addClass('hide');
					$("#present s").addClass('back');
				}

				updateBtnRechargeStatus();
			});

			i++;
		});
		
		if ($(bizs_quanguo).length <= 6) {
			if (!$(".jiayoubao .More").hasClass('hide')) {
				$(".jiayoubao .More").addClass('hide');
				$(".jiayoubao .More").removeClass('guan');
			}
		}
		if ($(bizs_quanguo).length > 6) {
			if ($(".jiayoubao .More").hasClass('hide')) {
				$(".jiayoubao .More").removeClass('hide');
				$(".jiayoubao .More").addClass('guan');
			}
		}
		if ($(bizs_prov).length <= 6) {
			if (!$(".prov .More").hasClass('hide')) {
				$(".prov .More").addClass('hide');
				$(".prov .More").removeClass('guan');
			}
		}
		if ($(bizs_prov).length > 6) {
			if ($(".prov .More").hasClass('hide')) {
				$(".prov .More").removeClass('hide');
				$(".prov .More").addClass('guan');
			}
		}

		$(".Slt").blur(function() {
					$(".Slt").children('img').hide();
					$(".Slt").removeClass("Slt");
				});

	}
	var bestCouponsTimeoutFunction = {
		fn : function() {
			swal("服务器连接错误，请检查网络重试！");
		}
	};

	var bestCouponsSuccess = {
		fn : function(json) {
			var Flag = json.Flag;
			if (Flag != null && Flag == 0) {
				//获取最优成功
				var usableCount = json.Data.usableCount;
				var bestCouponsRecord = json.Data.bestCouponsRecord;
				$(".couponsAll").addClass("hide");

				var usableCoupons = json.Data.crPagelist;
				$(usableCoupons).each(function(i, biz) {
							$("#coupons-items" + biz.id).removeClass("hide");
						});

				if (bestCouponsRecord != null && bestCouponsRecord != '') {
					//						$("#bestCouponsName").html(bestCouponsRecord.coupons.name + "（不返M币）");
					$("#bestCouponsName").html(bestCouponsRecord.coupons.name);
					$("#couponsDJ").html(bestCouponsRecord.coupons.freeFee
							/ 100);
					$("#couponsUsable").html(usableCount);
					$("#GM").html(bestCouponsRecord.naming.name);
					$("#selectCouponsId").html(bestCouponsRecord.couponsId);
					if ($("#coupons").hasClass('hide')) {

						$("#coupons").removeClass("hide");
						$("#Nocoupons").addClass("hide");
					}
					bindCouponsClick();
				} else {
					$("#couponsUsable").html(usableCount);
					if (!$("#coupons").hasClass('hide')) {

						$("#coupons").addClass("hide");
						$("#Nocoupons").removeClass("hide");
					}
					$("#coupons").unbind();
				}

			} else {
				swal(json.Message);
			}

		}
	};

	function getBestCoupons() {
		unbindCouponsClick();
		ajaxRequest(window.baseUrl + "/userWxPay/getBestCoupons", {
					businessId : _selected.id
				}, bestCouponsSuccess, bestCouponsTimeoutFunction);

	}

	function updateBtnRechargeStatus() {
		var disabled = !(_selected && _number);
		_btnRecharge.prop("disabled", disabled);
		if (!disabled) {
			_btnRecharge.unbind("click").bind('click', WXpayMoney);
		} else {
			_btnRecharge.unbind();
		}

		if (rechargeing) {
			_btnRecharge.prop("disabled", true);
		}
	}

	function confirm() {
		var body = $("<div></div>");
		if (_area.opCode == 1) {
			var remind = $('<div class="remind well" style="font-size:12px; line-height: 20px;">');
			remind.append('<i class="glyphicon glyphicon-info-sign icon"></i>');
			remind
					.append('<span class="text" style="display: inline-block; margin-bottom: 15px;">中国联通用户流量充值注意事项</span>');
			remind
					.append('<ul style="padding-left: 20px;">'
							+ '<li>该流量包仅限中国联通手机用户使用。</li>'
							+ '<li>该流量包支持用户包括3G手机套餐（不含20元预付费套餐）、2G手机套餐（除北京、云南、天津、四川、河北、黑龙江、辽宁、广西、江西）、4G手机套餐。</li>'
							+ '<li>该流量包当月一个用户可重复订购3次</li>'
							+ '<li>国内流量包生效时间为立即生效。生效后，流量包仅在生效当前自然月内有效，到期自动失效，不自动续订，未使用完的部分不能延续、累加至次月及以后各月使用。</li>'
							+ '<li>最终用户须处于正常可用状态下可订购：后付费用户处于紧急停机（挂失）、欠费停机、停机保号等状态时不可办理；预付费用户处于未激活期、充值期、锁定期不可办理。</li>'
							+ '</ul>');

			body.append(remind);
		}
		body.append("您确定要为手机号码 " + _number + " 充值 " + _selected.amountCount
				+ " 流量吗？");
		Modal.open({
					title : '确定充值',
					body : body,
					backdrop : true,
					okHandler : recharge
				});
	}

	function recharge() {
		$("#messager").empty();
		Modal.close();
		_btnRecharge.data("reset", _btnRecharge.text()).text(_btnRecharge
						.data("loading")).prop("disabled", true);

		Ajax.post(window.baseUrl + "/recharge!recharge.action", {
					amount : _selected.amount,
					range : _selected.range,
					phoneNumber : _number
				}, function(json) {
					if (Response.ok(json)) {
						if (json.Data) {
							var statusCode = json.Data.statusCode;
							if (statusCode == 200) {
								var data = json.Data.data;
								$("#messager").removeClass('text-success')
										.addClass('text-danger');
								if (!data) {
									$("#messager")
											.html("无法充值，但没有收到充值平台的响应，请联系客服");
								} else if (data.status == "0000") {
									$("#messager")
											.toggleClass('text-danger text-success')
											.html(	"订单成功提交，流量正在充值中");
								} else if (data.status == "1011") {
									$("#messager").html("无法充值，当前账户余额不足");
								} else if (data.status == "1015"
										|| data.status == "1016") {
									$("#messager").html("无法充值，充值的流量包已下线");
								} else if (data.status == "2002") {
									$("#messager").html("充值失败");
								} else {
									$("#messager").html("无法充值，返回错误码："
											+ data.status);
								}
							} else {
								$("#messager").html("无法充值，返回错误码：" + statusCode);
							}
						} else {
							$('#messager').html('暂时无法充值，请稍候再试');
						}
					} else {
						$("#messager").html(json.Message);
					}
					_btnRecharge.text(_btnRecharge.data("reset"));
					$('#amount').text('￥0');
					setTimeout(function() {
								$("#messager").empty();
								_selected = null;
								_selected_phoneValue = null;
								_package.find('li').removeClass('active');
								_btnRecharge.prop("disabled", false);
							}, 3000);
				});
	}

	function unbindCouponsClick() {

		$("#coupons").unbind();
	}

	function showCoupons() {
		if ($("#coupons").hasClass('hide')) {
			$("#coupons").removeClass("hide");
			$("#Nocoupons").addClass("hide");
		}
	}

	function showNoCoupons() {
		if (!$("#coupons").hasClass('hide')) {
			$("#coupons").addClass("hide");
			$("#Nocoupons").removeClass("hide");
			$("#Nocoupons").show();
		}

	}
	function intInputNumber() {
		//当清除号码时，重置参数
		_number = null;//充值号码重置
		_selected = null;//选中的包资源重置
		_selected_phoneValue = null;//包资源对应的话费值重置
		$(".mOne").empty();//普通包资源列表重置
		$(".mTwo").empty();//特价包资源列表重置

		$("#amount").text(0);//清除实际支付金额
		$("#present s").text();//清除原价值
	}

	$(function() {
				internal.init();
			});
})(jQuery);