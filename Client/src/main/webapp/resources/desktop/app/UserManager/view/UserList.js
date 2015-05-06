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
 
Ext.define('Wdesktop.app.UserManager.view.UserList', {
    extend: 'Ext.grid.Panel',
    alias : "widget.userList",
    frame: false,
    multiSelect : true,
    closable : true,
    emptyText: '没有相关数据',
    title : '用户列表',

    initComponent: function() {
        var me = this;
        me.store = me.getCreditsStore();
        Ext.applyIf(me, {
            viewConfig: {
                loadingText: '载入中',
                trackOver: true
            },
            selModel: Ext.create('Ext.selection.CheckboxModel', {

            }),
            dockedItems: [{
                    xtype: 'pagingtoolbar',
                    dock: 'bottom',
                    store: me.store,
                    displayInfo: true,
                    autoShow : true,
					autoDestroy : true
            }/*,{
            	xtype : 'toolbar',
				dock : 'top',
				layout : 'fit',
				defaults : {
					labelWidth : 40
				},
				items: [{
	            	xtype : 'modelSearch',
					parent : me
				}]
            }*/],
            columns: [{
                    xtype: 'gridcolumn',
                    align: 'center',
                    flex:1,
                    dataIndex : "id",
                    text: '账号'
            },{
                    xtype: 'gridcolumn',
                    flex:1,
                    align: 'center',
                    dataIndex : "name",
                    text: '姓名'
            },{
                    xtype: 'gridcolumn',
                    align: 'center',
                    text: '邮箱',
                    dataIndex : "email"
            },{
                xtype: 'gridcolumn',
                align: 'center',
                text: '积分',
                dataIndex : "credit"
            },{
	            xtype: 'gridcolumn',
	            align: 'center',
	            text: '注册时间',
	            dataIndex : "registerTime"
            },{
	            xtype: 'gridcolumn',
	            align: 'center',
	            text: '上次登录时间',
	            dataIndex : "lastLoginTime"
            },{
	            xtype: 'gridcolumn',
	            align: 'center',
	            text: '上次登录时间',
	            dataIndex : "lastLoginTime"
            },{
	            xtype: 'gridcolumn',
	            align: 'center',
	            text: '禁用',
	            renderer:function(v,m,r){
					return v?"是":"否";
				},
	            dataIndex : "disable"
            },{
	            xtype: 'gridcolumn',
	            align: 'center',
	            text: '激活',
	            renderer:function(v,m,r){
					return v?"是":"否";
				},
	            dataIndex : "vStatus"
            },{
	            xtype: 'gridcolumn',
	            align: 'center',
	            text: '角色',
	            dataIndex : "role"
            }],
			enableKeyNav : true,
			columnLines : true
        });
        me.callParent(arguments);
        me.store.load();
    },
    getCreditsStore:function(){
    	return Ext.create('Wdesktop.app.UserManager.store.UserStore');
    }
});
