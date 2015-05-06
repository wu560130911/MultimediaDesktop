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
 
Ext.define('Wdesktop.app.AppManager.view.ApplicationSearch', {
	extend : 'Ext.form.Panel',
	alias : 'widget.applicationSearch',

	requires : [ 'Ext.form.field.ComboBox', 'Ext.form.field.Date',
			'Ext.button.Button' ],

	height : 100,
	layout : 'absolute',
	closable : true,
	header: false,
	autoScroll: true,
	frameHeader: false,

	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			defaults : {
				labelWidth : 60
			},
			items : [ {
				xtype : 'combobox',
				x : 10,
				y : 10,
				width : 190,
				fieldLabel : '应用权限',
				store : me.roleStore,
				queryMode : 'local',
				displayField : 'name',
				name : 'role',
				valueField : 'name',
				allowBlank: false,
                editable: false
			}, {
				xtype : 'combobox',
				x : 220,
				y : 10,
				width : 190,
				fieldLabel : '应用类型',
				store : me.typeGroupStore,
				queryMode : 'local',
				displayField : 'name',
				name : 'typeGroup',
				valueField : 'name',
                editable: false
			}, {
				xtype : 'textfield',
				x : 430,
				y : 10,
				width : 190,
				name:'name',
				fieldLabel : '名称'
			}, {
				xtype : 'datefield',
				x : 10,
				y : 50,
				width : 190,
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
			}, {
				xtype : 'datefield',
				x : 220,
				y : 50,
				width : 190,
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
			}, {
				xtype : 'textfield',
				x : 430,
				y : 50,
				width : 190,
				name:'userId',
				fieldLabel : '用户'
			}, {
				xtype : 'button',
				x : 640,
				y : 10,
				height : 30,
				width : 70,
				text : '搜索',
				handler:me.onSearchButtonClick
			}, {
				xtype : 'button',
				x : 640,
				y : 50,
				height : 30,
				width : 70,
				text : '清空搜索',
				handler:me.onClearButtonClick
			} ]
		});

		me.callParent(arguments);
	},
	onSearchButtonClick : function() {
		var me = this, applicationSearch = me
				.up('applicationSearch'), applicationAllGrid = applicationSearch
				.parent;

		var form = applicationSearch.getForm();

		if (form.isValid()) {
			var store = applicationAllGrid.getStore();
			var data = {};
			var values = form.getValues();
			if (values['role'] != '') {
				data['role'] = values['role'];
			}
			if (values['name'] != '') {
				data['name'] = values['name'];
			}
			if (values['typeGroup'] != '') {
				data['typeGroup'] = values['typeGroup'];
			}
			if (values['userId'] != '') {
				data['userId'] = values['userId'];
			}
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
				.up('applicationSearch'), applicationAllGrid = applicationSearch
				.parent;
		var form = applicationSearch.getForm();
		var store = applicationAllGrid.getStore();
		form.reset();
		store.getProxy().extraParams = {};
		store.loadPage(1);
	}

});
