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
 
Ext.define('Wdesktop.app.systemLog.SystemLogSearch', {
    extend: 'Ext.form.Panel',

    requires: [
        'Ext.form.field.ComboBox',
        'Ext.form.field.Date',
        'Ext.button.Button'
    ],

    height: 144,
    bodyPadding: 10,
    frameHeader: false,
    header: false,

    layout: {
        type: 'table',
        columns: 3,
        tdAttrs: {
            margin: 5
        }
    },

    initComponent: function() {
        var me = this;
        
        me.logServerStore = me.getComboboxStore();
        me.leverStore = me.getLeverStore();
        
        Ext.applyIf(me, {
            defaults: {
                labelWidth: 60
            },
            items: [
                {
                    xtype: 'combobox',
                    fieldLabel: '服务器',
                    store : me.logServerStore,
    				queryMode : 'local',
    				displayField : 'name',
    				name : 'logFrom',
    				valueField : 'name',
    				allowBlank : false,
                    editable: false
                },
                {
                    xtype: 'textfield',
                    name : 'classPath',
                    fieldLabel: '类名'
                },
                {
                    xtype: 'textfield',
                    name : 'threadName',
                    fieldLabel: '线程名'
                },
                {
                    xtype: 'combobox',
                    fieldLabel: '日志级别',
                    store : me.leverStore,
    				queryMode : 'local',
    				displayField : 'name',
    				name : 'lever',
    				valueField : 'name',
                    editable: false
                },
                {
                    xtype: 'textfield',
                    colspan: 2,
                    name: 'message',
                    fieldLabel: '日志内容'
                },
                {
                    xtype: 'datefield',
                    fieldLabel: '开始',
                    name: 'startDate',
                    submitFormat: 'Y-m-d h:i:s',
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
                    fieldLabel: '结束',
                    name: 'endDate',
                    submitFormat: 'Y-m-d h:i:s',
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
                    xtype: 'combobox',
                    fieldLabel: '排序'
                },
                {
                    xtype: 'button',
                    colspan: 3,
                    frame: false,
                    height: 34,
                    width: 84,
                    text: '查询',
                    handler:me.onSearchClick
                }
            ]
        });

        me.callParent(arguments);
    },
    
    onSearchClick:function(){
    	var me = this,formPanel = me.up('form'),values = formPanel.getValues(), grid = formPanel.parent;
    	if(!formPanel.getForm().isValid()){
    		return;
    	}
    	var data = {};
		if (values['logFrom'] != '') {
			data['logFrom'] = values['logFrom'];
		}
		if (values['classPath'] != '') {
			data['classPath'] = values['classPath'];
		}
		if (values['lever'] != '') {
			data['lever'] = values['lever'];
		}
		if (values['threadName'] != '') {
			data['threadName'] = values['threadName'];
		}
		if (values['message'] != '') {
			data['message'] = values['message'];
		}
		if (values['startDate'] != '') {
			data['startDate'] = values['startDate'];
		}
		if (values['endDate'] != '') {
			data['endDate'] = values['endDate'];
		}
		grid.store.getProxy().extraParams = data;
		grid.store.loadPage(1);
    },
    
    getComboboxStore : function() {
		return Ext.create('Ext.data.Store', {
			proxy : {
				type : 'ajax',
				url : 'admin/systemLog/getLogServer.json',
				getMethod : function() {
					return 'POST';
				},
				reader : {
					type : 'json',
					root : 'servers'
				}
			},
			autoLoad : true,
			fields : [ {
				name : 'name'
			} ]
		});
	},
	getLeverStore : function() {
		return Ext.create('Ext.data.Store', {
			fields : [ {
				name : 'name'
			} ],
			data : [ {
				'name' : 'FATAL'
			}, {
				'name' : 'ERROR'
			}, {
				'name' : 'WARN'
			}, {
				'name' : 'INFO'
			}, {
				'name' : 'DEBUG'
			}, {
				'name' : 'TRACE'
			}, {
				'name' : 'ALL'
			}]
		});
	}
});
