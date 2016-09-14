Ext.define('CMS.view.operation.FluxSupplierManager', {
	extend : 'Ext.grid.Panel',
	xtype : 'fluxSupplierManagerView',
	store : 'CMS.store.operation.FluxSupplierStore',
	layout : 'fit',
	columnLines : true,
	border : false,
	plugins : [{
		ptype : 'rowediting',
		pluginId : 'fluxSupplierManager-rowediting',
        clicksToMoveEditor: 1,
        saveBtnText : '保存',
        cancelBtnText : '取消'
    }],
    selModel : new Ext.selection.CheckboxModel({
		mode : 'single',
		showHeaderCheckbox : false
	}),
	columns : [{
		header : 'ID',
		dataIndex : 'id',
		width : 50
	},{
		header : '供应商',
		dataIndex : 'supplierName',
		flex : 2,
		editor : {
			xtype : 'textfield',
			allowBlank : false,
			selectOnFocus : true
		}
	},{
		header : '邮箱',
		dataIndex : 'email',
		flex : 2,
		renderer : function(value, metadata) {
            metadata.tdAttr = 'data-qtip="' + value + '"';
            return value;
        },
		editor : {
			xtype : 'textfield',
//			allowBlank : false,
			selectOnFocus : true
		}
	},{
		header : '联系人',
		dataIndex : 'contact',
		flex : 1,
		editor : {
			xtype : 'textfield',
//			allowBlank : false,
			selectOnFocus : true
		}
	},{
		header : '联系电话',
		dataIndex : 'telephone',
		flex : 1,
		editor : {
			xtype : 'textfield',
//			allowBlank : false,
			selectOnFocus : true
		}
	}],
	tbar : [{
		text : '新建',
		name : 'add',
		iconCls : 'icon-add'
	},'-',{
		text : '删除',
		name : 'delete',
		iconCls : 'icon-delete'
	},'->',{
		xtype : 'displayfield',
		value : '供应商：'
	},{
		xtype : 'textfield',
		name : 'supplierName',
		width : 200,
		emptyText : '请输入'
	},{
		iconCls : 'icon-search',
		name : 'search',
		text : '查询'
	},{
		iconCls : 'icon-reset',
		name : 'reset',
		text : '重置'
	}],
	bbar : [{
		xtype : 'pagingtoolbar',
		store : 'CMS.store.operation.FluxSupplierStore',
		displayInfo: true
	}]
});