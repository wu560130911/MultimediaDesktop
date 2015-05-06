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
 
Ext.define('Wdesktop.app.AppManager.view.UserInstallApplication', {
	extend : 'Ext.grid.Panel',
	alias : "widget.userInstallApplication",
	requires : [ 'Ext.grid.View', 'Ext.toolbar.Paging',
			'Ext.selection.CheckboxModel', 'Ext.grid.column.Number',
			'Ext.grid.column.Template','Ext.grid.plugin.CellEditing'],

	closable : true,
	title : '安装的应用',
	titleAlign : 'center',
	columnLines : true,
	multiSelect : true,
	emptyText : '没有应用信息',

	initComponent : function() {
		var me = this;
		me.store = me.getApplicationStore();
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
					text : '卸载选中的应用',
					handler:me.onInstallButtonClick
				},{
					xtype : 'button',
					height : 30,
					text : '修改应用快捷方式',
					handler:me.onModifyButtonClick
				}]
			}],
			selModel : Ext.create('Ext.selection.CheckboxModel', {

			}),
			columns : [ {
				xtype : 'templatecolumn',
				tpl : '{application.name}',
				align : 'center',
				text : '名称',
				flex:1
			}, {
				xtype : 'templatecolumn',
				tpl : '{application.version}',
				align : 'center',
				text : '版本',
				flex:1
			}, {
				xtype : 'templatecolumn',
				tpl : '{application.userId}',
				align : 'center',
				text : '作者',
				flex:1
			}, {
				xtype : 'templatecolumn',
				tpl : '{application.useCount}',
				align : 'center',
				text : '使用人数'
			}, {
				xtype : 'datecolumn',
				align : 'center',
				text : '安装时间',
				format : 'Y-m-d',
				dataIndex : 'addDate',
				flex:1
			}, {
				xtype : 'templatecolumn',
				tpl : '{application.role}-{application.typeGroup}',
				text : '类型'
			},{
				text : '桌面',
				dataIndex: 'desktopIcon',
				renderer:function(v,m,r){
					return v?"是":"否";
				},
				editor: {
                    xtype: 'checkboxfield',
                    boxLabel: '是否添加'
                }
			},{
				text : '快速启动栏',
				dataIndex: 'quickStart',
				renderer:function(v,m,r){
					return v?"是":"否";
				},
				editor: {
                    xtype: 'checkboxfield',
                    boxLabel: '是否添加'
                }
			},{
				text : '开始菜单',
				dataIndex: 'startMenu',
				renderer:function(v,m,r){
					return v?"是":"否";
				},
				editor: {
                    xtype: 'checkboxfield',
                    boxLabel: '是否添加'
                }
			},{
				 xtype:'actioncolumn',
		         width:30,
		         items: [{
		        	icon:'resources/resources/image/uninstall.png',
		        	tooltip : '卸载应用',
		        	handler: function(grid, rowIndex, colIndex) {
		        		var rec = grid.getStore().getAt(rowIndex);
		        		var data = [];
		        		var app = {'id':rec.data.id};
        				data.push(Ext.JSON.encode(app));
		        		
		        		mainApplication.tools.ajaxTool.PostJsonData('userApp/deleteUserApp.json',"["+data.join(",")+"]",function(response,options){
		        			var data = Ext.JSON.decode(response.responseText);
		        			if(data['success']){
		        				grid.getStore().removeAt(rowIndex);
		        				mainApplication.desktop.refreshView();
		        				mainApplication.desktop.taskbar.removeMenuItemApp(rec.raw.application);
		        				mainApplication.desktop.taskbar.removeQuickStart(rec.raw.application);
		        			}else{
		        				Ext.MessageBox.alert('警告',data['error'],null,this);
		        			}
		        		});
		        	}
		         }]
			}],
			listeners : {
				containercontextmenu : {
					fn : me.onGridpanelContainerContextMenu,
					scope : me
				},
				itemcontextmenu : {
					fn : me.onGridpanelItemContextMenu,
					scope : me
				}
			},
            plugins: [
                      Ext.create('Ext.grid.plugin.CellEditing', {

                      })
                  ]
		});
		me.store.load();
		me.callParent(arguments);
	},
	getApplicationStore : function() {
		return Ext.create('Wdesktop.app.AppManager.store.UserApplicationStore');
	},
	createContextMenu : function() {
		var me = this;
		return Ext.create('Ext.menu.Menu', {
			width : 100,
			items : [ {
				text : '卸载应用',
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
		var gridPanel = Ext.ComponentQuery.query('userInstallApplication')[0];
		var apps = gridPanel.getSelectionModel().getSelection();
		
		if(apps.length <=0){
			Ext.MessageBox.alert('警告','请选择需要卸载的应用.',null,this);
			return;
		}
		
		var data = [];
		
		Ext.Array.each(apps,function(model){
			var app = {'id':model.data.id};
			data.push(Ext.JSON.encode(app));
			mainApplication.desktop.taskbar.removeMenuItemApp(model.raw.application);
			mainApplication.desktop.taskbar.removeQuickStart(model.raw.application);
		});
		
		mainApplication.tools.ajaxTool.PostJsonData('userApp/deleteUserApp.json',"["+data.join(",")+"]",function(response,options){
			var data = Ext.JSON.decode(response.responseText);
			if(data['success']){
				gridPanel.getStore().remove(apps);
				mainApplication.desktop.refreshView();
			}else{
				Ext.MessageBox.alert('警告',data['error'],null,this);
			}
		});
	},
	onModifyButtonClick:function(){
		var gridPanel = Ext.ComponentQuery.query('userInstallApplication')[0];
		var apps = gridPanel.getStore().getModifiedRecords();
		if(apps.length <=0){
			return;
		}
		var data = [];
		Ext.Array.each(apps,function(model){
			var desktopIcon = model.data.desktopIcon,quickStart= model.data.quickStart,startMenu= model.data.startMenu;
			var num = 0;
			if(quickStart){ 
				num = num+1;
				mainApplication.desktop.taskbar.addQuickStart(model.raw.application);
			}
			if(startMenu) {
				num = num+2;
				mainApplication.desktop.taskbar.addMenuItemApp(model.raw.application);
			}
			if(desktopIcon) num = num+4;
			var app = {'id':model.data.id,'type':num};
			data.push(Ext.JSON.encode(app));
		});
		mainApplication.tools.ajaxTool.PostJsonData('userApp/changeAppsIcon.json',"["+data.join(",")+"]",function(response,options){
			var data = Ext.JSON.decode(response.responseText);
			if(data['success']){
				gridPanel.getStore().commitChanges();
				// 这儿可以实现自动刷新，但是因为毕设时间紧，所以暂时不实现
				mainApplication.desktop.refreshView();
			}else{
				Ext.MessageBox.alert('警告',data['error'],null,this);
			}
		});
	}

});
