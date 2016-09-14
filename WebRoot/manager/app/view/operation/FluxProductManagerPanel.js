Ext.define('CMS.view.operation.FluxProductManagerPanel', {
	extend : 'Ext.form.Panel',
	xtype : 'fluxProductManagerPanel',
	id : 'fluxProductManagerPanel',
	requires : ['CMS.model.operation.FluxProductModel'],
	plain : true,
	regionType : '',
	provList : '',
	fieldDefaults : {
		labelWidth : 50,
		anchor : '100%'
	},
	layout : {
		type : 'vbox',
		align : 'stretch'
	},
	initComponent : function() {
		var me = this;
		
		var pstore = this.productstore = Ext.create('Ext.data.Store', {
			model : 'CMS.model.operation.FluxProductModel',
			//autoLoad:true,
			proxy : {
				type : 'ajax',
				url : '../operation/product_list.action',
				reader : {
					type : 'json',
					total : 'total',
					root : 'products'
				}
			}
		});
		
		var columns = {
			items : [{
				text : '产品名',
				dataIndex : 'productName',
				flex : 2,
				editor : {
					xtype : 'textfield',
					allowBlank : false,
					selectOnFocus : true
				}
			}, {
				text : '流量大小',
				dataIndex : 'fluxAmount',
				flex : 1,
				editor : {
					xtype : 'numberfield',
					allowBlank : false,
					selectOnFocus : true
				}
			}, {
				text : '售价',
				dataIndex : 'salePrice',
				flex : 1,
				editor : {
					xtype : 'numberfield',
					allowBlank : false,
					selectOnFocus : true
				}
			}, {
				text : '市场价',
				dataIndex : 'marketPrice',
				flex : 1,
				editor : {
					xtype : 'numberfield',
					allowBlank : false,
					selectOnFocus : true
				}
			}, {
				text : '省份',
				dataIndex : 'province',
				flex : 1,
				editor : {
					xtype : 'combobox',
			    	store : Ext.create('Ext.data.Store', {
					    fields: ['name', 'value'],
					    data : this.provList
					}),
			    	displayField : 'name',
			    	valueField : 'value',
			    	name: 'provincesArray',
			    	queryMode: 'local',
			    	editable : false,
			    	allowBlank : false
				}
			}]
		};
//		if(this.regionType == 1) {//全国包
//			columns = {
//				items : [{
//					text : '产品名',
//					dataIndex : 'productName',
//					flex : 2,
//					editor : {
//						xtype : 'textfield',
//						allowBlank : false,
//						selectOnFocus : true
//					}
//				}, {
//					text : '流量大小',
//					dataIndex : 'fluxAmount',
//					flex : 1,
//					editor : {
//						xtype : 'numberfield',
//						allowBlank : false,
//						selectOnFocus : true
//					}
//				}, {
//					text : '售价',
//					dataIndex : 'salePrice',
//					flex : 1,
//					editor : {
//						xtype : 'numberfield',
//						allowBlank : false,
//						selectOnFocus : true
//					}
//				}, {
//					text : '市场价',
//					dataIndex : 'marketPrice',
//					flex : 1,
//					editor : {
//						xtype : 'numberfield',
//						allowBlank : false,
//						selectOnFocus : true
//					}
//				}]
//			}
//		} else {//省包
//			
//		};
		
		this.gridpanel = Ext.create('Ext.grid.Panel',{
			xtype : 'gridpanel',
			columnLines : true,
			multiSelect : true,
			selModel : new Ext.selection.CheckboxModel,
			store : pstore,
			columns : columns,
			plugins : [{
				ptype : 'cellediting',
				pluginId : 'fluxProductManagerPanel-cellediting',
		        clicksToEdit: 1
		    }],
			tbar : [ {
				text : '添加产品',
				iconCls : 'icon-add',
				name : 'add'
			}, {
				text : '删除产品',
				iconCls : 'icon-delete',
				name : 'delete'
			} ]
		});
		
		this.items = [ {
			xtype : 'textfield',
			fieldLabel : '序号',
			name : 'packageId',
			hidden : true
		}, {
			xtype : 'displayfield',
			fieldLabel : '包名',
			readOnly : true,
			name : 'packageName',
			allowBlank : false
		}, this.gridpanel];
		
		this.callParent(arguments);
	}
});