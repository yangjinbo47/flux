Ext.define('CMS.store.operation.FluxWxOrderStore',{
	extend : 'Ext.data.Store',
	model : 'CMS.model.operation.FluxWxOrderModel',
	autoLoad : true,
	pageSize : pageSize,
    proxy : {
		type : 'ajax',
		url : '../operation/wxorder_list.action',
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