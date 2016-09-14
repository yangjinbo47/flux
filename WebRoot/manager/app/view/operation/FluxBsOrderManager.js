Ext.define('CMS.view.operation.FluxBsOrderManager', {
	extend : 'Ext.grid.Panel',
	xtype : 'fluxBsOrderManagerView',
	store : 'CMS.store.operation.FluxBsOrderStore',
	layout : 'fit',
	columnLines : true,
	border : false,
	columns : [{
		header : '订单号',
		dataIndex : 'orderId',
		flex : 1
	},{
		header : '包名',
		dataIndex : 'packageName',
		flex : 1,
		renderer : function(value, metadata) {
            metadata.tdAttr = 'data-qtip="' + value + '"';
            return value;
        }
	},{
		header : '产品名',
		dataIndex : 'subject',
		flex : 1
	},{
		header : '手机号',
		dataIndex : 'msisdn',
		flex : 1
	},{
		header : '流量值',
		dataIndex : 'fluxAmount',
		flex : 1
	},{
		header : '金额（分）',
		dataIndex : 'fee',
		flex : 1
	},{
		header : '状态',
		dataIndex : 'status',
		width : 100,
		renderer : function(value) {
			if(value == '0'){
				return '成功';
			} else if (value == '1') {
				return '充值中';
			} else if (value == '2') {
				return '失败未退款';
			} else if (value == '2001') {
				return '失败退款中';
			} else if (value == '2002') {
				return '失败已退款';
			}
		}
	},{
		header : '时间',
		dataIndex : 'createTime',
		flex : 1
	}],
	tbar : ['->',{
		xtype : 'displayfield',
		value : '手机号：'
	},{
		xtype : 'textfield',
		name : 'msisdn',
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
		store : 'CMS.store.operation.FluxBsOrderStore',
		displayInfo: true
	}]
});