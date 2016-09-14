Ext.define('CMS.view.operation.FluxProductManagerWindow', {
	extend : 'Ext.window.Window',
	xtype : 'fluxProductManamgerWindow',
	id : 'fluxProductManamgerWindow',
	title : '添加产品信息',
	modal : true,
	resizable : false,
	width : 800,
	height : 500,
	layout : 'fit',
	constrainHeader : true,
	dockedItems : [ {
		xtype : 'toolbar',
		dock : 'bottom',
		ui : 'footer',
		layout : {
			pack : 'center'
		},
		items : [ {
			minWidth : 80,
			text : '保存',
			name : 'save'
		}, {
			minWidth : 80,
			text : '取消',
			name : 'cancel'
		} ]
	} ]
});
