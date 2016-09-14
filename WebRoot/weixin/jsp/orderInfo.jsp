<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html lang="zh-cn">
<head>
	<title>订单详情</title>
	<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no" />
<script src="../weixin/js/jquery.js"></script>
<script src="../weixin/js/bootstrap.js"></script>
<script src="../weixin/js/sweetalert.js"></script> 
<script src="../weixin/js/json2.js"></script>

<link href="../weixin/css/bootstrap.css" rel="stylesheet"/>
<link href="../weixin/css/sweetalert.css" rel="stylesheet"/>
<link href="../weixin/css/userweb.global.css" rel="stylesheet"/>
<link href="../weixin/css/button-touch.css" rel="stylesheet"/>
<style>
body {background-color: #F2F2F2; padding-bottom: 65px;}
</style>
<script src="../weixin/js/global.js"></script> 
<script src="../weixin/js/mopote.lib.js"></script> 
<script src="../weixin/js/mopote.lib.ui.button.js"></script> 
<script src="../weixin/js/noty.js"></script> 
<script src="../weixin/js/jquery-util.js?v=2015090100"></script> 
<script src="../weixin/js/wx-fen-xiang.js?v=2016011200"></script> 
<script src="../weixin/js/common.js"></script> 
<script src="../weixin/js/wx-share.js"></script> 
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script> 
<link href="../weixin/css/commom.css" rel="stylesheet"/>
</head>
<body class="bg">
	<nav class="navType-c">
		<s:if test="#request.isSucc == true">
			<div class="navItem">
				<h2>订单详情</h2>
				<p>订单号：${fluxOrder.orderId}</p>
				<p>订单时间：${fluxOrder.createTime}</p>
				<p>充值号码：${fluxOrder.msisdn}</p>
				<p>充值面额：${fluxOrder.subject}</p>
				<p>支付方式：微信支付</p>
				<s:if test="#request.fluxOrder.status == 0">
					<p>充值状态：充值成功</p>
				</s:if>
				<s:elseif test="#request.fluxOrder.status == 1">
					<p>充值状态：充值中</p>
				</s:elseif>
				<s:elseif test="#request.fluxOrder.status == 2">
					<p>充值状态：失败</p>
				</s:elseif>
				<s:elseif test="#request.fluxOrder.status == 2001">
					<p>充值状态：失败退款中</p>
				</s:elseif>
				<s:elseif test="#request.fluxOrder.status == 2002">
					<p>充值状态：失败已退款</p>
				</s:elseif>
				<p>实际支付：￥${fluxOrder.fee/100}</p>
			</div>
		</s:if>
		<s:else>
			<div class="navItem">
				<h2>未找到订单</h2>
			</div>
		</s:else>
		
		<!--
		<div class="navItem">
			<h2>未找到订单</h2>
		</div>
		-->
		<div class="container order-footer relative">
			<p>若流量超过48小时未到账，请联系客服400-888-8888</P>
		</div>
	</nav>
</body>
</html>