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
 
Ext.define('Wdesktop.app.AppManager.view.UnCheckApplication', {
	extend : 'Ext.grid.Panel',
	alias : "widget.unCheckApplication",
	requires : [ 'Ext.grid.View', 'Ext.toolbar.Paging',
			'Ext.selection.CheckboxModel', 'Ext.grid.column.Number',
			'Ext.grid.column.Template'],

	closable : true,
	title : '待审核的应用',
	titleAlign : 'center',
	columnLines : true,
	multiSelect : true,
	emptyText : '没有应用信息',

	initComponent : function() {
		var me = this;
		me.store = me.getApplicationStore();
		Ext.applyIf(me, {
			dockedItems : [ {
				xtype : 'pagingtoolbar',
				dock : 'bottom',
				store : me.store,
				displayInfo : true
			}],
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
                url: 'app/listUnCheck.json',
                reader: {
                    type: 'json',
                    root: 'apps'
                }
            }
		});
	},
	onGridpanelContainerContextMenu : function(dataview, e, eOpts) {
		e.stopEvent();
	},

	onGridpanelItemContextMenu : function(dataview, record, item, index, e,
			eOpts) {
		e.stopEvent();
	}

});
