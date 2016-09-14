Ext.define('CMS.store.operation.FluxBsOrderStore',{
	extend : 'Ext.data.Store',
	model : 'CMS.model.operation.FluxBsOrderModel',
	autoLoad : true,
	pageSize : pageSize,
    proxy : {
		type : 'ajax',
		url : '../operation/bsorder_list.action',
		reader : {
			type : 'json',
			total : 'total',
			root : 'orders'
		},
		actionMethods:{
			read:'POST'
		}
	}
});