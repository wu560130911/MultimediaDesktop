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
 
Ext.define('Wdesktop.app.AppManager.view.AdminCheckApplicationGrid', {
	extend : 'Ext.grid.Panel',
	alias : "widget.adminCheckApplicationGrid",
	requires : [ 'Ext.grid.View', 'Ext.toolbar.Paging',
			'Ext.selection.CheckboxModel', 'Ext.grid.column.Number',
			'Ext.grid.column.Template',
			'Wdesktop.app.AppManager.view.ApplicationSearch'],

	closable : true,
	title : '审核提交的应用',
	titleAlign : 'center',
	columnLines : true,
	multiSelect : true,
	emptyText : '没有应用信息',

	initComponent : function() {
		var me = this;
		me.store = me.getApplicationStore();
		me.typeGroupStore = me.getTypeGroupStore();
		me.comboboxStore = me.getComboboxStore();
		me.contextMenu = me.createContextMenu();
		Ext.applyIf(me, {
			dockedItems : [ {
				xtype : 'pagingtoolbar',
				dock : 'bottom',
				store : me.store,
				displayInfo : true,
				items : [ {
					xtype : 'button',
					height : 30,
					text : '审核通过(选中的应用)',
					handler:me.onInstallButtonClick
				}]
			}, {
				xtype : 'toolbar',
				dock : 'top',
				layout : 'fit',
				defaults : {
					labelWidth : 40
				},
				items : [ {
					xtype : 'applicationSearch',
					roleStore : me.comboboxStore,
					typeGroupStore : me.typeGroupStore,
					parent : me
				// onSearchButtonClick : me.onSearchButtonClick,
				// onClearButtonClick : me.onClearButtonClick
				} ]
			} ],
			selModel : Ext.create('Ext.selection.CheckboxModel', {

			}),
			columns : [ {
				xtype : 'gridcolumn',
				align : 'center',
				dataIndex : 'name',
				text : '名称'
			}, {
				xtype : 'gridcolumn',
				align : 'center',
				dataIndex : 'version',
				text : '版本'
			}, {
				xtype : 'gridcolumn',
				align : 'center',
				dataIndex : 'iconCls',
				text : '样式'
			}, {
				xtype : 'datecolumn',
				align : 'center',
				text : '时间',
				format : 'Y-m-d',
				dataIndex : 'addDate'
			}, {
				xtype : 'gridcolumn',
				align : 'center',
				dataIndex : 'userId',
				text : '作者'
			}, {
				xtype : 'templatecolumn',
				tpl : '{role}-{typeGroup}',
				text : '类型'
			}, {
				xtype : 'gridcolumn',
				align : 'center',
				dataIndex : 'tip',
				text : '提示',
				flex : 1
			}, {
				xtype : 'gridcolumn',
				align : 'center',
				dataIndex : 'description',
				text : '描述',
				flex : 1
			},{
				 xtype:'actioncolumn',
		         width:30,
		         items: [{
		        	icon:'resources/resources/image/uninstall.png',
		        	tooltip : '审核通过',
		        	handler: function(grid, rowIndex, colIndex) {
		        		var rec = grid.getStore().getAt(rowIndex);
		        		var data = [];
		        		var app = {'id':rec.data.id};
        				data.push(Ext.JSON.encode(app));
		        		
		        		mainApplication.tools.ajaxTool.PostJsonData('admin/app/checkApp.json',"["+data.join(",")+"]",function(response,options){
		        			var data = Ext.JSON.decode(response.responseText);
		        			if(data['success']){
		        				grid.getStore().removeAt(rowIndex);
		        			}else{
		        				Ext.MessageBox.alert('警告',data['error'],null,this);
		        			}
		        		});
		        	}
		         }]
			} ],
			listeners : {
				containercontextmenu : {
					fn : me.onGridpanelContainerContextMenu,
					scope : me
				},
				itemcontextmenu : {
					fn : me.onGridpanelItemContextMenu,
					scope : me
				}
			}
		});
		me.store.load();
		me.callParent(arguments);
	},
	getApplicationStore : function() {
		return Ext.create('Wdesktop.app.AppManager.store.ApplicationStore',{
			proxy: {
                type: 'ajax',
                url: 'admin/app/getUnCheckApp.json',
                reader: {
                    type: 'json',
                    root: 'apps'
                }
            }
		});
	},
	getComboboxStore : function() {
		return Ext.create('Ext.data.Store', {
			proxy : {
				type : 'ajax',
				url : 'auth/getRoles.json',
				getMethod : function() {
					return 'POST';
				},
				reader : {
					type : 'json',
					root : 'data'
				}
			},
			autoLoad : true,
			fields : [ {
				name : 'name'
			} ]
		});
	},
	getTypeGroupStore : function() {
		return Ext.create('Ext.data.Store', {
			fields : [ {
				name : 'name'
			} ],
			data : [ {
				'name' : '文字'
			}, {
				'name' : '管理'
			}, {
				'name' : '休闲'
			}, {
				'name' : '娱乐'
			}, {
				'name' : '游戏'
			}, {
				'name' : '音乐'
			}, {
				'name' : '视频'
			}, {
				'name' : '电影'
			}, {
				'name' : '其他'
			} ]
		});
	},
	createContextMenu : function() {
		var me = this;
		return Ext.create('Ext.menu.Menu', {
			width : 100,
			items : [ {
				text : '审核通过',
				iconCls : 'media_collect',
				handler:me.onInstallButtonClick
			} ]
		});
	},
	onGridpanelContainerContextMenu : function(dataview, e, eOpts) {
		e.stopEvent();
	},

	onGridpanelItemContextMenu : function(dataview, record, item, index, e,
			eOpts) {
		var me = this;
		e.stopEvent();
		me.contextMenu.showAt(e.getXY());
	},
	onInstallButtonClick:function(){
		var gridPanel = Ext.ComponentQuery.query('adminCheckApplicationGrid')[0];
		var apps = gridPanel.getSelectionModel().getSelection();
		if(apps.length <=0){
			Ext.MessageBox.alert('警告','请选择审核通过的应用.',null,this);
			return;
		}
		var data = [];
		
		Ext.Array.each(apps,function(model){
			var app = {'id':model.data.id};
			data.push(Ext.JSON.encode(app));
		});
		
		mainApplication.tools.ajaxTool.PostJsonData('admin/app/checkApp.json',"["+data.join(",")+"]",function(response,options){
			var data = Ext.JSON.decode(response.responseText);
			if(data['success']){
				gridPanel.getStore().remove(apps);
			}else{
				Ext.MessageBox.alert('警告',data['error'],null,this);
			}
		});
		
	}

});
