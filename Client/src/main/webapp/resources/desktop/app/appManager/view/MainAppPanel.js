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
 
Ext.define('Wdesktop.app.AppManager.view.MainAppPanel', {
	extend : 'Ext.panel.Panel',
	alias : "widget.appManagerMainAppPanel",
	frame : false,
	layout : {
		type : 'border'
	},

	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			items : [ {
				xtype : 'treepanel',
				region : 'west',
				width : 192,
				title : '我的应用市场',
				split : true,
				frame : false,
				collapsible : true,
				columnLines : false,
				lines : false,
				useArrows : true,
				rootVisible : false,
				store:Ext.create('Wdesktop.app.AppManager.store.AppTreeStore'),
				tools:[{
					type:'expand',
					handler:function(event,toolEl,owner,tool){
						tool.up('treepanel').expandAll();
					}
				},{
					type:'collapse',
					handler:function(event,toolEl,owner,tool){
						tool.up('treepanel').collapseAll();
					}
				}],
				viewConfig: {
	            	plugins: [
	            	    Ext.create('Ext.tree.plugin.TreeViewDragDrop', {
	                        nodeHighlightOnDrop: true,
	                        nodeHighlightOnRepair: true
	                })]
	            }
			}, {
				xtype : 'tabpanel',
				region : 'center',
				plugins: [Ext.create('Ext.ux.TabScrollerMenu'),
	                        Ext.create('Ext.ux.TabCloseMenu')],
				activeTab : 0,
				items : [ {
					xtype : 'panel',
					title : '应用市场'
				} ]
			} ]
		});

		me.callParent(arguments);
	}

});
