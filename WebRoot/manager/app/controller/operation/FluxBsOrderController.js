Ext.define('CMS.controller.operation.FluxBsOrderController', {
	extend : 'Ext.app.Controller',
	views : ['CMS.view.operation.FluxBsOrderManager'],
	stores : ['CMS.store.operation.FluxBsOrderStore'],
	refs : [{
		ref : 'fluxBsOrderManagerView',
		selector : 'fluxBsOrderManagerView'
	}],
	init : function() {
		this.control({
			'fluxBsOrderManagerView > toolbar > button[name=search]' : {
				click : this.search
			},
			'fluxBsOrderManagerView > toolbar > button[name=reset]' : {
				click : this.reset
			}
		});
	},
	
	search : function() {
		var grid = this.getFluxBsOrderManagerView();
		var queryParams = {};
		queryParams.msisdn = this.getFluxBsOrderManagerView().down('toolbar > textfield[name=msisdn]').getValue();
		grid.getStore().proxy.extraParams = queryParams;
		grid.getStore().reload();
		grid.getView().refresh();
	},
	
	reset : function() {
		var grid = this.getFluxBsOrderManagerView();
		var msisdnField = grid.down('toolbar > textfield[name=msisdn]');
		
		grid.getStore().proxy.extraParams = {};
		grid.getStore().load();
		grid.getView().refresh();
		
		msisdnField.reset();
	}
});
