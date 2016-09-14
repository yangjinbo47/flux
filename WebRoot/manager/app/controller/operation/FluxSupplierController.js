Ext.define('CMS.controller.operation.FluxSupplierController', {
	extend : 'Ext.app.Controller',
	views : ['CMS.view.operation.FluxSupplierManager'],
	stores : ['CMS.store.operation.FluxSupplierStore'],
	refs : [{
		ref : 'fluxSupplierManagerView',
		selector : 'fluxSupplierManagerView'
	}],
	init : function() {
		this.control({
			'fluxSupplierManagerView' : {
				render : function(grid) {
					var roweditplugin = grid.getPlugin('fluxSupplierManager-rowediting');
					roweditplugin.on({
						canceledit : this.roweditcancel,
					    edit: this.roweditupdate,
					    scope: this
					});
				}
			},
			'fluxSupplierManagerView > toolbar > button[name=add]' : {
				click : this.onAddClick
			},
			'fluxSupplierManagerView > toolbar > button[name=delete]' : {
				click : this.onDelClick
			},
			'fluxSupplierManagerView > toolbar > button[name=search]' : {
				click : this.search
			},
			'fluxSupplierManagerView > toolbar > button[name=reset]' : {
				click : this.reset
			}
		});
	},
	
	onAddClick : function() {
		var grid = this.getFluxSupplierManagerView();
		var roweditplugin = grid.getPlugin('fluxSupplierManager-rowediting');
		roweditplugin.cancelEdit();
		var r = Ext.create('CMS.model.operation.FluxSupplierModel', {
            supplierName: '供应商',
            email:'邮箱',
            contact:'联系人',
            telephone:'联系电话'
        });
        
        grid.store.insert(0, r);
        roweditplugin.startEdit(0,0);
	},
	
	onDelClick : function() {
		var grid = this.getFluxSupplierManagerView();
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
						url : '../operation/supplier_delete.action',
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
		
		var grid = this.getFluxSupplierManagerView();
		var sm = grid.getSelectionModel();
		var rs = sm.getSelection();
		
		Ext.each(grid.getSelectionModel().getSelection(), function(row, index, value) {
			var id;
			if(row.data.id){
				id = row.data.id;
			}
			
			Ext.Ajax.request({
				url : '../operation/supplier_save.action',
				params : {
					'id' : id,
					'supplierName' : row.data.supplierName,
					'email' : row.data.email,
					'contact' : row.data.contact,
					'telephone' : row.data.telephone
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
		var grid = this.getFluxSupplierManagerView();
		grid.store.reload();
	},
	
	search : function() {
		var grid = this.getFluxSupplierManagerView();
		var queryParams = {};
		queryParams.supplierName = this.getFluxSupplierManagerView().down('toolbar > textfield[name=supplierName]').getValue();
		grid.getStore().proxy.extraParams = queryParams;
		grid.getStore().reload();
		grid.getView().refresh();
	},
	
	reset : function() {
		var grid = this.getFluxSupplierManagerView();
		var supplierNameField = grid.down('toolbar > textfield[name=supplierName]');
		
		grid.getStore().proxy.extraParams = {};
		grid.getStore().load();
		grid.getView().refresh();
		
		supplierNameField.reset();
	}
});
