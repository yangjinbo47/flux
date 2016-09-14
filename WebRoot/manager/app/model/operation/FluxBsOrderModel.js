Ext.define('CMS.model.operation.FluxBsOrderModel', {
	extend : 'Ext.data.Model',
	fields : ['id', 'orderId', 'subject', 'productId', 'packageId', 'msisdn', 'oid', 'fee', 'openId', 'fluxAmount', 'status', 'createTime',
			'packageName', 'productName']
});