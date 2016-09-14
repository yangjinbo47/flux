Ext.define('CMS.store.operation.FluxPackageStore',{
	extend : 'Ext.data.Store',
	model : 'CMS.model.operation.FluxPackageModel',
	autoLoad : true,
	pageSize : pageSize,
    proxy : {
		type : 'ajax',
		url : '../operation/package_list.action',
		reader : {
			type : 'json',
			total : 'total',
			root : 'packages'
		},
		actionMethods:{
			read:'POST'
		}
	}
});