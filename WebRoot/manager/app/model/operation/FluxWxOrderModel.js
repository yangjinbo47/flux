Ext.define('CMS.model.operation.FluxWxOrderModel', {
	extend : 'Ext.data.Model',
	fields : ['id', 'orderId', 'prepayId', 'productId', 'packageId', 'msisdn', 'subject', 'fee', 'openId', 'payStatus', 'payMsg', 'wxRefundId', 'refundStatus', 'refundMsg', 'createTime',
			'packageName', 'productName']
});