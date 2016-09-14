Ext.define('CMS.model.operation.FluxPackageModel', {
	extend : 'Ext.data.Model',
	fields : ['id', 'name', 'mid', 'pno', 'secret', 'flux', 'cost', 'supplierId', 'supplierName', 'region', 'operator', 'provinces', 'provincesArray', 'status', 'ps', 'createTime']
});