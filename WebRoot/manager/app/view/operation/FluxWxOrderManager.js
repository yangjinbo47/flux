Ext.define('CMS.view.operation.FluxWxOrderManager', {
	extend : 'Ext.grid.Panel',
	xtype : 'fluxWxOrderManagerView',
	store : 'CMS.store.operation.FluxWxOrderStore',
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
		header : '金额（分）',
		dataIndex : 'fee',
		flex : 1
	},{
		header : '支付状态',
		dataIndex : 'payStatus',
		width : 80,
		renderer : function(value) {
			if(value == '1'){
				return '未支付';
			} else if (value == '3'){
				return '成功';
			} else if (value == '4'){
				return '失败';
			}
		}
	},{
		header : '支付状态',
		dataIndex : 'refundStatus',
		width : 80,
		renderer : function(value) {
			if(value == '1'){
				return '未退款';
			} else if (value == '3'){
				return '退款成功';
			} else if (value == '4'){
				return '退款失败';
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
		store : 'CMS.store.operation.FluxWxOrderStore',
		displayInfo: true
	}]
});