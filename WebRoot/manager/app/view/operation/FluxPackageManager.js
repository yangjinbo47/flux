Ext.define('CMS.view.operation.FluxPackageManager', {
	extend : 'Ext.grid.Panel',
	xtype : 'fluxPackageManagerView',
	store : 'CMS.store.operation.FluxPackageStore',
	layout : 'fit',
	columnLines : true,
	border : false,
	plugins : [{
		ptype : 'rowediting',
		pluginId : 'fluxPackageManager-rowediting',
        clicksToMoveEditor: 1,
        saveBtnText : '保存',
        cancelBtnText : '取消'
    }],
    selModel : new Ext.selection.CheckboxModel({
		mode : 'single',
		showHeaderCheckbox : false
	}),
	columns : [ {
		header : 'ID',
		dataIndex : 'id',
		width : 50
	},{
		header : '包名',
		dataIndex : 'name',
		width : 150,
		renderer : function(value, metadata) {
            metadata.tdAttr = 'data-qtip="' + value + '"';
            return value;
        },
		editor : {
			xtype : 'textfield',
			allowBlank : false,
			selectOnFocus : true
		}
	},{
		header : '供应商',
		dataIndex : 'supplierName',
		width : 200,
		renderer : function(value, metadata) {
            metadata.tdAttr = 'data-qtip="' + value + '"';
            return value;
        },
		editor : {
			xtype : 'combobox',
	    	store : 'CMS.store.operation.FluxSupplierStore',
	    	displayField : 'supplierName',
	    	valueField : 'id',
	    	name: 'supplierId',
	    	queryMode: 'remote',
	    	allowBlank : false,
	    	editable : false
		}
	},{
		header : '商户号',
		dataIndex : 'mid',
		width : 80,
		editor : {
			xtype : 'textfield',
			allowBlank : false,
			selectOnFocus : true
		},
		renderer : function(value, metadata) {
            metadata.tdAttr = 'data-qtip="' + value + '"';
            return value;
        }
	},{
		header : '产品名',
		dataIndex : 'pno',
		width : 100,
		editor : {
			xtype : 'textfield',
			allowBlank : false,
			selectOnFocus : true
		},
		renderer : function(value, metadata) {
            metadata.tdAttr = 'data-qtip="' + value + '"';
            return value;
        }
	},{
		header : 'key',
		dataIndex : 'secret',
		width : 100,
		editor : {
			xtype : 'textfield',
			allowBlank : false,
			selectOnFocus : true
		},
		renderer : function(value, metadata) {
            metadata.tdAttr = 'data-qtip="' + value + '"';
            return value;
        }
	},{
		header : '流量大小(M)',
		dataIndex : 'flux',
		width : 120,
		editor : {
			xtype : 'numberfield',
			allowBlank : false,
			selectOnFocus : true
		}
	},{
		header : '已使用',
		dataIndex : 'cost',
		width : 100
	},{
		header : '区域',
		dataIndex : 'region',
		width : 80,
		editor : {
			xtype : 'combobox',
	    	store : Ext.create('Ext.data.Store', {
			    fields: ['id', 'name'],
			    data : [
			        {"id":1, "name":"全国包"},
			        {"id":2, "name":"省包"}
			    ]
			}),
	    	displayField : 'name',
	    	valueField : 'id',
	    	name: 'region',
	    	queryMode: 'local',
	    	editable : false,
	    	allowBlank : false
		},
		renderer : function(value) {
			if(value == '1'){
				return '全国包';
			} else {
				return '省包';
			}
		}
	},{
		header : '售卖省份',
		dataIndex : 'provinces',
		width : 120,
		renderer : function(value, metadata) {
            metadata.tdAttr = 'data-qtip="' + value + '"';
            return value;
        },
        editor : {
			xtype : 'combobox',
	    	store : Ext.create('Ext.data.Store', {
			    fields: ['name', 'value'],
			    data : [
			        {name:'河北', value:'河北'},
			        {name:'山西', value:'山西'},
			        {name:'辽宁', value:'辽宁'},
			        {name:'吉林', value:'吉林'},
			        {name:'黑龙江', value:'黑龙江'},
			        {name:'江苏', value:'江苏'},
			        {name:'浙江', value:'浙江'},
			        {name:'安徽', value:'安徽'},
			        {name:'福建', value:'福建'},
			        {name:'江西', value:'江西'},
			        {name:'山东', value:'山东'},
			        {name:'河南', value:'河南'},
			        {name:'湖北', value:'湖北'},
			        {name:'湖南', value:'湖南'},
			        {name:'广东', value:'广东'},
			        {name:'海南', value:'海南'},
			        {name:'四川', value:'四川'},
			        {name:'贵州', value:'贵州'},
			        {name:'云南', value:'云南'},
			        {name:'陕西', value:'陕西'},
			        {name:'甘肃', value:'甘肃'},
			        {name:'青海', value:'青海'},
			        {name:'内蒙古', value:'内蒙古'},
			        {name:'广西', value:'广西'},
			        {name:'西藏', value:'西藏'},
			        {name:'宁夏', value:'宁夏'},
			        {name:'新疆', value:'新疆'},
			        {name:'北京', value:'北京'},
			        {name:'天津', value:'天津'},
			        {name:'上海', value:'上海'},
			        {name:'重庆', value:'重庆'}
			    ]
			}),
	    	displayField : 'name',
	    	valueField : 'value',
	    	name: 'provincesArray',
	    	queryMode: 'local',
	    	multiSelect: true
		}
	},{
		header : '运营商',
		dataIndex : 'operator',
		width : 70,
		editor : {
			xtype : 'combobox',
	    	store : Ext.create('Ext.data.Store', {
			    fields: ['id', 'name'],
			    data : [
			        {"id":1, "name":"移动"},
			        {"id":2, "name":"联通"},
			        {"id":3, "name":"电信"}
			    ]
			}),
	    	displayField : 'name',
	    	valueField : 'id',
	    	name: 'operator',
	    	queryMode: 'local',
	    	editable : false,
	    	allowBlank : false
		},
		renderer : function(value) {
			if(value == '1'){
				return '移动';
			} else if(value == '2') {
				return '联通';
			} else {
				return '电信';
			}
		}
	},{
		header : '备注',
		dataIndex : 'ps',
		width : 100,
        editor : {
			xtype : 'combobox',
	    	store : Ext.create('Ext.data.Store', {
			    fields: ['name', 'value'],
			    data : [
			        {name:'年包', value:'年包'},
			        {name:'立即生效', value:'立即生效'},
			        {name:'当月有效', value:'当月有效'}
			    ]
			}),
	    	displayField : 'name',
	    	valueField : 'value',
	    	name: 'ps',
	    	queryMode: 'local',
	    	editable : false,
	    	multiSelect: false
		},
		renderer : function(value, metadata) {
            metadata.tdAttr = 'data-qtip="' + value + '"';
            return value;
        }
	},{
		header : '状态',
		dataIndex : 'status',
		width : 50,
		editor : {
			xtype : 'combobox',
	    	store : Ext.create('Ext.data.Store', {
			    fields: ['id', 'name'],
			    data : [
			        {"id":1, "name":"正常"},
			        {"id":0, "name":"冻结"}
			    ]
			}),
	    	displayField : 'name',
	    	valueField : 'id',
	    	name: 'status',
	    	queryMode: 'local',
	    	editable : false,
	    	allowBlank : false
		},
		renderer : function(value) {
			if(value == '1'){
				return '正常';
			} else {
				return '冻结';
			}
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
	},'-',{
		text : '添加产品',
		name : 'productAdd',
		iconCls : 'icon-edittask'
	},'->',{
		xtype : 'displayfield',
		value : '包名：'
	},{
		xtype : 'textfield',
		name : 'packageName',
		width : 130,
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
		store : 'CMS.store.operation.FluxPackageStore',
		displayInfo: true
	}]
});