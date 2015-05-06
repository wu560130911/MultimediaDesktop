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
 
Ext.define('Wdesktop.app.UserManager.controller.UserController', {
	extend : 'Ext.app.Controller',

	views : [ 'MainAppPanel','UserInfo','UserModify','UserModifyPassword','UserModifyEmail','CreditList','Search','LoginLogList','UserList','UserOnlineList' ],
	models : [ ],
	stores : [],

	refs : [ {
		ref : 'treepanel',
		selector : 'treepanel[title="用户管理"] > treeview'
	} ],
	init : function(application) {
		
		var me = this;
		me.IntemContextMenu = me.getItemcontextmenu();
		
		this.control({
			'treepanel[title="用户管理"] > treeview' : {
				cellclick : this.onTreeCellClick
			},'userOnlineList':{
				containercontextmenu:function(panel,e,opts){
	        		e.stopEvent();
	        		me.IntemContextMenu.showAt(e.getXY());
	        	},
	        	itemcontextmenu:function(panel,record,item,index,e,opts){
	        		e.stopEvent();
	        		me.IntemContextMenu.showAt(e.getXY());
	        	}
			},'menu[itemId=userOnlineList] menuitem[text=强制下线]':{
				click:me.onForceLogoutClick
			}
		});
	},
	
	onForceLogoutClick:function(){
		var userOnlineList = Ext.ComponentQuery.query('userOnlineList')[0];
		var videos = userOnlineList.getSelectionModel().getSelection();
		if(videos.length <=0){
			Ext.MessageBox.alert('警告','请先选择会话.',null,this);
			return;
		}
		var data = [];
		Ext.Array.each(videos,function(model){
			var app = {'id':model.data.id};
			data.push(Ext.JSON.encode(app));
		});
		mainApplication.tools.ajaxTool.PostJsonData('admin/userManager/forceLogout.json',"["+data.join(",")+"]",function(response,options){
			var data = Ext.JSON.decode(response.responseText);
			if(data['success']){
				userOnlineList.getStore().remove(videos);
			}else{
				Ext.MessageBox.alert('警告',data['error'],null,this);
			}
		});
	},
	
	getItemcontextmenu:function(){
    	var contextmenu = Ext.create('Ext.menu.Menu',{
    		width: 100,
    		itemId: 'userOnlineList'
    	});
    	contextmenu.add({text:'强制下线',iconCls:'media_collect'});
    	return contextmenu;
    },
	
	onTreeCellClick : function(tableview, td, cellIndex, record, tr, rowIndex,
			e, eOpts) {
		if (record.raw.aliasname && record.raw.leaf) {
			var tabpanel = tableview.up('userManagerMainAppPanel').down(
					'tabpanel');
			var panel = Ext.ComponentQuery.query(record.raw.aliasname);
			if (panel == '') {
				panel = Ext.widget(record.raw.aliasname);
				tabpanel.add(panel).show();
				tabpanel.setActiveTab(panel);
			} else {
				tabpanel.setActiveTab(panel[0]);
			}
		}else if(record.raw.leaf){
			Ext.MessageBox.alert('提示','该功能被管理员关闭',null,this);
		}
	}

});
