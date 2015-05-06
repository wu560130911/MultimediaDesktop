/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
Ext.define('Wdesktop.app.UserManager.view.Search', {
    extend: 'Ext.form.Panel',

    alias : 'widget.modelSearch',
    
    requires: [
        'Ext.form.field.Date',
        'Ext.button.Button'
    ],

    height: 50,
    width: 628,
    layout: 'absolute',
    bodyPadding: 10,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            defaults: {
                labelWidth: 60
            },
            items: [
                {
                    xtype: 'datefield',
                    x: 10,
                    y: 10,
                    width: 190,
                    fieldLabel : '开始',
    				name:'startDate',
    				editable: false,
    				submitFormat: 'Y-m-d',
    				validator : function(value) {
    					var me = this;
    					var date = me.up("form").query(
    							"datefield");
    					if (date[1].getValue() == null
    							|| date[0].getValue() <= date[1]
    									.getValue()) {
    						if (date[1].hasActiveError()) {
    							date[1].isValid();
    						}
    						return true;
    					}
    					return "结束时间必须大于开始时间";
    				},
    				maxValue : new Date()
                },
                {
                    xtype: 'datefield',
                    x: 220,
                    y: 10,
                    width: 180,
                    fieldLabel : '结束',
    				submitFormat: 'Y-m-d',
    				editable: false,
    				name:'endDate',
    				validator : function(value) {
    					var me = this;
    					var date = me.up("form").query(
    							"datefield");
    					if (date[1].getValue() == null
    							|| date[0].getValue() <= date[1]
    									.getValue()) {
    						if (date[0].hasActiveError()) {
    							date[0].isValid();
    						}
    						return true;
    					}
    					return "结束时间必须大于开始时间";
    				},
    				maxValue : new Date()
                },
                {
                    xtype: 'button',
                    x: 410,
                    y: 10,
                    text: '搜索',
    				handler:me.onSearchButtonClick
                },
                {
                    xtype: 'button',
                    x: 480,
                    y: 10,
                    text: '清空',
    				handler:me.onClearButtonClick
                }
            ]
        });

        me.callParent(arguments);
    },
	onSearchButtonClick : function() {
		var me = this, applicationSearch = me
				.up('modelSearch'), applicationAllGrid = applicationSearch
				.parent;

		var form = applicationSearch.getForm();

		if (form.isValid()) {
			var store = applicationAllGrid.getStore();
			var data = {};
			var values = form.getValues();
			if (values['startDate'] != '') {
				data['startDate'] = Ext.Date.format(new Date(
						values['startDate']), 'Y-m-d');
			}
			if (values['endDate'] != '') {
				data['endDate'] = Ext.Date.format(new Date(
						values['endDate']), 'Y-m-d');
			}
			store.getProxy().extraParams = data;
			store.loadPage(1);
		}

	},
	onClearButtonClick : function() {
		var me = this, applicationSearch = me
				.up('modelSearch'), applicationAllGrid = applicationSearch
				.parent;
		var form = applicationSearch.getForm();
		var store = applicationAllGrid.getStore();
		form.reset();
		store.getProxy().extraParams = {};
		store.loadPage(1);
	}

});
