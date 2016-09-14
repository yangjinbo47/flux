Ext.define('CMS.controller.operation.FluxPackageController', {
	extend : 'Ext.app.Controller',
	views : ['CMS.view.operation.FluxPackageManager'],
	stores : ['CMS.store.operation.FluxPackageStore','CMS.store.operation.FluxSupplierStore'],
	refs : [{
		ref : 'fluxPackageManagerView',
		selector : 'fluxPackageManagerView'
	},{
		ref : 'fluxProductManamgerWindow',
		selector : 'fluxProductManamgerWindow'
	}],
	init : function() {
		this.control({
			'fluxPackageManagerView' : {
				render : function(grid) {
					var roweditplugin = grid.getPlugin('fluxPackageManager-rowediting');
					roweditplugin.on({
						canceledit : this.roweditcancel,
					    edit: this.roweditupdate,
					    scope: this
					});
				}
			},
			'fluxPackageManagerView > toolbar > button[name=add]' : {
				click : this.onAddClick
			},
			'fluxPackageManagerView > toolbar > button[name=delete]' : {
				click : this.onDelClick
			},
			'fluxPackageManagerView > toolbar > button[name=productAdd]' : {//添加产品
				click : this.onProductAddClick
			},
			'fluxProductManamgerWindow > fluxProductManagerPanel > gridpanel > toolbar > button[name=add]' : {//产品窗口添加产品按钮
				click : this.addProduct
			},
			'fluxProductManamgerWindow > fluxProductManagerPanel > gridpanel > toolbar > button[name=delete]' : {//产品窗口删除产品按钮
				click : this.delProduct
			},
			'fluxProductManamgerWindow > toolbar > button[name=save]' : {//产品窗口保存
				click : this.productWindowSave
			},
			'fluxProductManamgerWindow > toolbar > button[name=cancel]' : {//产品窗口保存
				click : this.productWindowCancel
			},
			'fluxPackageManagerView > toolbar > button[name=search]' : {
				click : this.search
			},
			'fluxPackageManagerView > toolbar > button[name=reset]' : {
				click : this.reset
			}
		});
	},
	
	onAddClick : function() {
		var grid = this.getFluxPackageManagerView();
		var roweditplugin = grid.getPlugin('fluxPackageManager-rowediting');
		roweditplugin.cancelEdit();
		var r = Ext.create('CMS.model.operation.FluxPackageModel', {
            name:'包名',
            mid:'商户号',
            pno:'产品号',
            flux:'1024',
            cost:'已使用流量',
            secret:'key',
            supplierId:'供应商',
            supplierName:'供应商',
            region:'区域',
            operator:'运营商',
            provinces:'',
            provincesArray:'',
            status:'状态',
            ps:'',
            createTime:'创建时间'
        });
        
        grid.store.insert(0, r);
        roweditplugin.startEdit(0,0);
	},
	
	onDelClick : function() {
		var grid = this.getFluxPackageManagerView();
		var sm = grid.getSelectionModel();
		var rs = sm.getSelection();
		if (!rs.length) {
			Ext.Msg.alert('提示', '请选择要删除的记录');
			return;
		} else {
			Ext.Msg.confirm("请确认", "是否真的要删除指定的内容", function(button, text) {
				if (button == "yes") {
					var ids = '';
					Ext.each(grid.getSelectionModel().getSelection(), function(row, index, value) {
						ids = ids + row.data.id + ',';
					});
					ids = ids.slice(0, -1);
					Ext.Ajax.request({
						url : '../operation/package_delete.action',
						params : {
							'ids' : ids
						},
						success : function(action) {
							var respText = Ext.JSON.decode(action.responseText);
							Ext.Msg.alert('提示', respText.msg);
							grid.getStore().reload();
						},
						failure : function(action) {
							var respText = Ext.JSON.decode(action.responseText);
							Ext.Msg.alert('提示', respText.msg);
						}
					});
				}
			});
		}
	},
	
	onProductAddClick : function() {
		var grid = this.getFluxPackageManagerView();
		var sm = grid.getSelectionModel();
		var rs = sm.getSelection();
		if (!rs.length) {
			Ext.Msg.alert('提示', '请选择一个需要添加的流量包!');
			return;
		} else if (rs.length > 1) {
			Ext.Msg.alert('提示', '只能选择一个流量包进行关联!');
			return;
		} else {
			var packageid = rs[0].data.id;
			var provJsonArray = [];
			var provArray = rs[0].data.provincesArray;
			var result = provArray.filter(function(item, index, array) {
				var provJson = {};
				provJson.name = item;
				provJson.value = item;
				provJsonArray.push(provJson);
			});
			var panel = Ext.create('CMS.view.operation.FluxProductManagerPanel',{provList:provJsonArray});
			panel.productstore.proxy.extraParams.packageId = packageid;
			panel.productstore.loadPage(1);
			
			with(panel.form){
				findField('packageId').setValue(rs[0].data.id);
				findField('packageName').setValue(rs[0].data.name);
			}
			var win = Ext.create('CMS.view.operation.FluxProductManagerWindow');
			win.add(panel);
			win.show();
		}
	},
	
	addProduct : function() {
		var r = Ext.create('CMS.model.operation.FluxProductModel', {
//			id:'-1',
            productName:'产品名',
            fluxAmount:'30',
            salePrice:'285',
            marketPrice:'300',
            province:'省份'
        });

        var grid = this.getFluxProductManamgerWindow().down('fluxProductManagerPanel > gridpanel');
        grid.store.insert(0, r);
	},
	
	delProduct : function() {
		var grid = this.getFluxProductManamgerWindow().down('fluxProductManagerPanel > gridpanel');
		var sm = grid.getSelectionModel();
		var rs = sm.getSelection();
		if (!rs.length) {
			Ext.Msg.alert('提示', '请选择相应的数据行');
			return;
		} else {
			Ext.Msg.confirm("请确认", "是否真的要删除指定的内容", function(button, text) {
				if (button == "yes") {
					Ext.Array.each(rs, function(record) {
						grid.getStore().remove(record);
					});
				}
			});
		}
	},
	
	productWindowSave : function() {
		var me=this;
		var form = this.getFluxProductManamgerWindow().down('fluxProductManagerPanel').getForm();
		if (!form.isValid()) {
			return;
		}
		
		var grid = this.getFluxProductManamgerWindow().down('fluxProductManagerPanel > gridpanel');
		var store = grid.getStore();
		if (!store.getCount()) {
			Ext.Msg.alert('提示', '请添加产品！');
			return;
		}
		var packageId = this.getFluxProductManamgerWindow().down('fluxProductManagerPanel > textfield[name=packageId]').getValue();
		var count = store.getCount();
		var rs = store.getRange(0,count-1);
		var products = [],productJson = {};
		Ext.Array.each(rs, function(record) {
			products.push({
//				id : record.data.id,
				packageId : packageId,
				productName : record.data.productName,
				fluxAmount : record.data.fluxAmount,
				salePrice : record.data.salePrice,
				marketPrice : record.data.marketPrice,
				province : record.data.province
			})
		});
		productJson.products = products;
		Ext.Ajax.request({
			url : '../operation/product_save.action',
			params : {
				'packageId' : packageId,
				'products' : JSON.stringify(productJson)
			},
			success : function(action) {
				var respText = Ext.JSON.decode(action.responseText);
				Ext.Msg.alert('提示', respText.msg);
				//关闭窗口
				me.getFluxProductManamgerWindow().close();
			},
			failure : function(action) {
				Ext.Msg.alert('提示', '系统故障，删除失败，请和系统管理员联系');
			}
		});
	},
	
	productWindowCancel : function() {
		this.getFluxProductManamgerWindow().close();
	},
	
	roweditupdate : function(plugin) {
		var me = this;
		if(!plugin.editor.isValid()){
			plugin.editor.body.highlight("fb7a7a", {
			    attr: "backgroundColor",
			    easing: 'easeIn',
			    duration: 1000
			});
			return false;
		}
		
		var grid = this.getFluxPackageManagerView();
		var sm = grid.getSelectionModel();
		var rs = sm.getSelection();
		
		Ext.each(grid.getSelectionModel().getSelection(), function(row, index, value) {
			var supplierId;
			if (isNaN(row.data.supplierId)){//非数字
				Ext.Msg.alert('温馨提示','请选择供应商');
				plugin.startEdit(0,0);
				return;
			} else {//数字
				supplierId = row.data.supplierId;
			}
			
			var region;
			if (isNaN(row.data.region)){//非数字
				Ext.Msg.alert('温馨提示','请选择区域');
				plugin.startEdit(0,0);
				return;
			} else {//数字
				region = row.data.region;
			}
			
			var operator;
			if (isNaN(row.data.operator)){//非数字
				Ext.Msg.alert('温馨提示','请选择运营商');
				plugin.startEdit(0,0);
				return;
			} else {//数字
				operator = row.data.operator;
			}
			
			var ps;
			if (row.data.ps == ''){//未选择
				Ext.Msg.alert('温馨提示','请选择备注');
				plugin.startEdit(0,0);
				return;
			} else {//数字
				ps = row.data.ps;
			}
			
			var status;
			if (isNaN(row.data.status)){//非数字
				Ext.Msg.alert('温馨提示','请选择状态');
				plugin.startEdit(0,0);
				return;
			} else {//数字
				status = row.data.status;
			}
			
			var id;
			if(row.data.id){
				id = row.data.id;
			}
			
			Ext.Ajax.request({
				url : '../operation/package_save.action',
				params : {
					'id' : id,
					'name' : row.data.name,
					'mid' : row.data.mid,
					'pno' : row.data.pno,
					'flux' : row.data.flux,
					'cost' : row.data.cost,
					'supplierId' : row.data.supplierId,
					'region' : row.data.region,
					'operator' : row.data.operator,
					'provincesArray' : row.data.provincesArray,
					'ps' : row.data.ps,
					'status' : row.data.status
				},
				success : function(action) {
					var respText = Ext.JSON.decode(action.responseText);
					var success = respText.success;
					Ext.Msg.alert(success == true ? "成功" : "失败", respText.msg);
					if(success) {
						grid.getSelectionModel().clearSelections();
						grid.getStore().reload();
					}
				},
				failure : function(action) {
					Ext.Msg.alert('提示', respText.msg);
				}
			});
		});
		
	},
	
	roweditcancel : function() {
		var grid = this.getFluxPackageManagerView();
		grid.store.reload();
	},
	
	search : function() {
		var grid = this.getFluxPackageManagerView();
		var queryParams = {};
		queryParams.packageName = this.getFluxPackageManagerView().down('toolbar > textfield[name=packageName]').getValue();
		grid.getStore().proxy.extraParams = queryParams;
		grid.getStore().reload();
		grid.getView().refresh();
	},
	
	reset : function() {
		var grid = this.getFluxPackageManagerView();
		var packageNameField = grid.down('toolbar > textfield[name=packageName]');
		
		grid.getStore().proxy.extraParams = {};
		grid.getStore().load();
		grid.getView().refresh();
		
		packageNameField.reset();
	}
});
