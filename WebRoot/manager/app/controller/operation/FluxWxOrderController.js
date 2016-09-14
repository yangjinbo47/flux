Ext.define('CMS.controller.operation.FluxWxOrderController', {
	extend : 'Ext.app.Controller',
	views : ['CMS.view.operation.FluxWxOrderManager'],
	stores : ['CMS.store.operation.FluxWxOrderStore'],
	refs : [{
		ref : 'fluxWxOrderManagerView',
		selector : 'fluxWxOrderManagerView'
	}],
	init : function() {
		this.control({
			'fluxWxOrderManagerView > toolbar > button[name=search]' : {
				click : this.search
			},
			'fluxWxOrderManagerView > toolbar > button[name=reset]' : {
				click : this.reset
			}
		});
	},
	
	search : function() {
		var grid = this.getFluxWxOrderManagerView();
		var queryParams = {};
		queryParams.msisdn = this.getFluxWxOrderManagerView().down('toolbar > textfield[name=msisdn]').getValue();
		grid.getStore().proxy.extraParams = queryParams;
		grid.getStore().reload();
		grid.getView().refresh();
	},
	
	reset : function() {
		var grid = this.getFluxWxOrderManagerView();
		var msisdnField = grid.down('toolbar > textfield[name=msisdn]');
		
		grid.getStore().proxy.extraParams = {};
		grid.getStore().load();
		grid.getView().refresh();
		
		msisdnField.reset();
	}
});
