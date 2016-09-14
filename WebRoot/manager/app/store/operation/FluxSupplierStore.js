/**
 * 包月推送渠道store
 */
Ext.define('CMS.store.operation.FluxSupplierStore',{
	extend : 'Ext.data.Store',
	model : 'CMS.model.operation.FluxSupplierModel',
	autoLoad : true,
	pageSize : pageSize,
    proxy : {
		type : 'ajax',
		url : '../operation/supplier_list.action',
		reader : {
			type : 'json',
			total : 'total',
			root : 'suppliers'
		},
		actionMethods:{
			read:'POST'
		}
	}
});