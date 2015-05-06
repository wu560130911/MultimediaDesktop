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
 
Ext.define('Wdesktop.app.AppManager.controller.AppController', {
	extend : 'Ext.app.Controller',

	views : [ 'MainAppPanel', 'AddMyApplication', 'ApplicationAllGrid',
			'Wdesktop.app.AppManager.view.HotRankApplication',
			'UserInstallApplication', 'UnCheckApplication', 'CheckApplication',
			'AdminCheckApplicationGrid','AdminManagerApplicationGrid' ],
	models : [ 'ApplicationModel' ],
	stores : [],

	refs : [ {
		ref : 'treepanel',
		selector : 'treepanel[title="我的应用市场"] > treeview'
	} ],
	init : function(application) {
		this.control({
			'treepanel[title="我的应用市场"] > treeview' : {
				cellclick : this.onTreeCellClick
			}
		});
	},
	onTreeCellClick : function(tableview, td, cellIndex, record, tr, rowIndex,
			e, eOpts) {
		if (record.raw.aliasname && record.raw.leaf) {
			var tabpanel = tableview.up('appManagerMainAppPanel').down(
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
