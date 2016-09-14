(function($) {
	
	var internal = {
		init : function() {
			var pageNo = 1;
			var url = "../internal/business_orderlist.action";
			var param = {
				pageNo : pageNo
			};
			var success = {
				fn : function (data, status) {
					
					var data = data.Data;
					var retCode = data.retCode;
					var errorMsg = data.errorMsg;
					if(retCode==0){
						var page = data.page;
						var result = page.result;
						if(result.length == 0){
							swal("没有更多了");
						}else{
							$(result).each(function(i, item) {
								var text ='';
								text +='<div class="itemorder navStyle-d">';
								text +='<a href="../internal/business_queryOrderInfo?orderId=' + item.orderId + '">';
								text +='<ul class="orderList">';
								text +='<li>';
								text +='<span>'+item.msisdn+'</span>';
								text +='<span class="s_txt">'+item.createTime.substring(6)+'</span>';
								text +='</li>';
								text +='<li>';
								text +='<span>' + item.subject+'</span>';
								
								if(item.status==0){
									text +='<span class="s_txt c_1">充值成功</span>';
								}
								else if(item.status==1){
									text +='<span class="s_txt c_3">充值中</span>';
								}
								else if(item.status==2){
									text +='<span class="s_txt c_2">失败未退款</span>';
								}
								else if(item.status==2001){
									text +='<span class="s_txt c_2">失败退款中</span>';
								}
								else if(item.status==2002){
									text +='<span class="s_txt c_2">失败已退款</span>';
								}
								
								text +='</li>';
								text +='<li>';
								text +='<p>￥'+item.fee/100+'</p>';
								text +='</li>';
								text +='</ul>';
								text +='</a>';
								text +='</div>';
								$J(".itemorder:last").after(text);	
			           		});
		           			
		           			$J("#pageNo").val(page.pageNo);
						}
					}else{
						swal(errorMsg);
					}
						  
				}
			};
			ajaxRequest(url, param, success,timeoutFunction);
		}
	};
	
	$(function() {
		internal.init();
	});
})(jQuery);