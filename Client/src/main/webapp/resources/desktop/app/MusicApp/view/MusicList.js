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
 
Ext.define('Wdesktop.app.MusicApp.view.MusicList', {
    extend: 'Ext.grid.Panel',
    alias : "widget.musiclist",
    frame: true,
    multiSelect : true,
    emptyText: '没有相关数据',

    initComponent: function() {
        var me = this;
        me.store = me.getMusicsStore();
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
					autoDestroy : true,
					items: [{
	                    xtype: 'button',
	                    text: '播放',
	                    iconCls:'media_play'
	                }]
            },{
            	xtype : 'toolbar',
				dock : 'top',
				layout : 'fit',
				defaults : {
					labelWidth : 40
				},
				items: [{
	            	xtype : 'musicSearch',
	            	typeStore : me.getMusicTypeStore(),
					parent : me
				}]
            }],
            columns: [{
                    xtype: 'gridcolumn',
                    align: 'center',
                    flex:1,
                    dataIndex : "title",
                    text: '歌曲名称'
            },{
                    xtype: 'gridcolumn',
                    flex:1,
                    align: 'center',
                    dataIndex : "singer",
                    text: '歌手'
            },{
                    xtype: 'numbercolumn',
                    align: 'center',
                    text: '年份',
                    dataIndex : "year",
                    format: '0000',
                    width:80,
                    renderer:function(value){
						return Ext.String.format("{0}年",value);
					}
            },{
            	xtype: 'gridcolumn',
                align: 'center',
                dataIndex : "type",
                flex:1,
                text: '音乐类型'
            },{
            	xtype: 'numbercolumn',
                align: 'center',
                dataIndex : "duration",
                text: '时间长度',
                format: '0',
                renderer:function(value){
					return Ext.String.format("{0}分{1}秒",(value-value%60)/60,value%60);
				}
            },{
            	xtype: 'numbercolumn',
                align: 'center',
                dataIndex : "size",
                text: '文件大小',
                format: '0',
                flex:1,
                renderer:function(value){
                	return Ext.String.format("{0}KB",(value/1024.0).toFixed(2));
				}
            },{
            	xtype: 'gridcolumn',
                align: 'center',
                dataIndex : "time",
                text: '上传时间',
                flex:1,
                width:140
            },{
            	xtype: 'gridcolumn',
            	align: 'center',
            	text: '上传用户',
            	flex:1,
            	dataIndex : "userId"
            }],
			enableKeyNav : true,
			columnLines : true
        });
        me.callParent(arguments);
        me.store.load();
    },
    getMusicsStore:function(){
    	return Ext.create('Wdesktop.app.MusicApp.store.MusicStore');
    },
    getMusicTypeStore:function(){
    	return Ext.create('Ext.data.Store', {
			fields : [ 'name' ],
			data : [{
				name : "古典音乐"
			}, {
				name : "宗教音乐"
			}, {
				name : "流行音乐"
			}, {
				name : "重金属音乐"
			}, {
				name : "摇滚乐"
			}, {
				name : "电子音乐"
			}, {
				name : "爵士乐"
			},{
				name : "其他"
			}]
    	});
    },
});
