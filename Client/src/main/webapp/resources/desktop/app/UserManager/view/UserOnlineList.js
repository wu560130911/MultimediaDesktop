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
 
Ext.define('Wdesktop.app.UserManager.view.UserOnlineList', {
	extend : 'Ext.grid.Panel',
	alias : "widget.userOnlineList",
	frame : false,
	multiSelect : true,
	closable : true,
	emptyText : '没有相关数据',
	title : '在线用户列表',

	initComponent : function() {
		var me = this;
		me.store = me.getCreditsStore();
		Ext.applyIf(me, {
			viewConfig : {
				loadingText : '载入中',
				trackOver : true
			},
			selModel : Ext.create('Ext.selection.CheckboxModel', {

			}),
			dockedItems : [ {
				xtype : 'pagingtoolbar',
				dock : 'bottom',
				store : me.store,
				displayInfo : true,
				autoShow : true,
				autoDestroy : true
			}],
			columns : [ {
				xtype : 'gridcolumn',
				align : 'center',
				flex : 1,
				dataIndex : "id",
				text : '回话ID'
			}, {
				xtype : 'gridcolumn',
				align : 'center',
				dataIndex : "userid",
				text : '用户账号'
			}, {
				xtype : 'gridcolumn',
				align : 'center',
				dataIndex : "username",
				text : '用户姓名'
			}, {
				xtype : 'gridcolumn',
				align : 'center',
				text : '登录时间',
				dataIndex : "startTime"
			}, {
				xtype : 'gridcolumn',
				align : 'center',
				text : '上次访问时间',
				dataIndex : "lastAccessTime"
			}, {
				xtype : 'gridcolumn',
				align : 'center',
				text : '登录IP',
				dataIndex : "host"
			} ,{
				xtype:'actioncolumn',
				width:80,
				items: [{
					icon:'resources/resources/images/exit.png',
					tooltip: '结束该会话',
					handler:me.closeSessionHandler
				}]
			}],
			enableKeyNav : true,
			columnLines : true
		});
		me.callParent(arguments);
		me.store.load();
	},
	getCreditsStore : function() {
		return Ext.create('Wdesktop.app.UserManager.store.UserOnlineStore');
	},
	closeSessionHandler:function(grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var data = [];
		var app = {'id':rec.data.id};
		data.push(Ext.JSON.encode(app));
		mainApplication.tools.ajaxTool.PostJsonData('admin/userManager/forceLogout.json',"["+data.join(",")+"]",function(response,options){
			var data = Ext.JSON.decode(response.responseText);
			if(data['success']){
				Ext.MessageBox.alert('提示','强制下线成功',null,this);
			}else{
				Ext.MessageBox.alert('警告',data['error'],null,this);
			}
		});
	}
});
