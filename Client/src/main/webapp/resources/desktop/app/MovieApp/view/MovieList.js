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
 
Ext.define('Wdesktop.app.MovieApp.view.MovieList', {
    extend: 'Ext.grid.Panel',
    alias : "widget.movielist",
    requires:['Ext.ux.ProgressBarPager'],
    frame: false,
    multiSelect : true,
    emptyText: '没有相关数据',

    initComponent: function() {
        var me = this;
        me.store = me.getMoviesStore();
        Ext.applyIf(me, {
            viewConfig: {
                loadingText: '载入中',
                trackOver: true,
                plugins: [
                  Ext.create('Ext.grid.plugin.DragDrop', {
                  })
                ]
            },
            selModel: Ext.create('Ext.selection.CheckboxModel', {

            }),
            dockedItems: [{
                    xtype: 'pagingtoolbar',
                    dock: 'bottom',
                    store: me.store,
                    width: 360,
                    pageSize:25,
                    displayInfo: true,
                    autoShow : true,
					autoDestroy : true,
					plugins: Ext.create('Ext.ux.ProgressBarPager', {}),
					items: [{
	                    xtype: 'button',
	                    text: '播放',
	                    iconCls:'media_play'
	                },{
	                	xtype: 'button',
	                    text: '查看详细信息',
	                    iconCls:'video_more_info'
	                }]
            },{
            	xtype : 'toolbar',
				dock : 'top',
				layout : 'fit',
				defaults : {
					labelWidth : 40
				},
				items: [{
	            	xtype : 'movieSearch',
	            	typeStore : me.getMovieTypeStore(),
					parent : me
				}]
            }],
            columns: [{
                    xtype: 'gridcolumn',
                    align: 'center',
                    dataIndex : "title",
                    text: '影片名称',
                    flex:1
            },{
            	xtype: 'gridcolumn',
                align: 'center',
                dataIndex : "actor",
                text: '演员',
                flex:1
            },{
                    xtype: 'gridcolumn',
                    align: 'center',
                    dataIndex : "author",
                    text: '导演'
            },{
                    xtype: 'datecolumn',
                    align: 'center',
                    text: '年份',
                    format : 'Y-m-d',
                    dataIndex : "madetime",
            },{
            	xtype: 'gridcolumn',
                align: 'center',
                dataIndex : "type",
                text: '影片类型'
            },{
            	xtype: 'numbercolumn',
                align: 'center',
                dataIndex : "duration",
                text: '时间长度',
                format: '0',
                flex:1,
                renderer:function(value){
                	var second = value%60;
                	var minute = ((value-second)/60)%60;
                	var hours = (value-second-minute*60)/3600;
					return Ext.String.format("{0}小时{1}分{2}秒",hours,minute,second);
				}
            },{
            	xtype: 'numbercolumn',
                align: 'center',
                dataIndex : "size",
                text: '文件大小',
                format: '0',
                renderer:function(value){
					return Ext.String.format("{0}KB",(value/1024.0).toFixed(2));
				}
            },{
            	xtype: 'gridcolumn',
                align: 'center',
                dataIndex : "time",
                text: '上传时间',
                width:140
            },{
            	xtype: 'gridcolumn',
            	align: 'center',
            	text: '上传用户',
            	dataIndex : "userId"
            }],
			enableKeyNav : true,
			columnLines : true
        });

        me.callParent(arguments);
        me.store.load();
    },
    
    getMoviesStore:function(){
    	return Ext.create('Wdesktop.app.MovieApp.store.MovieStore');
    },
    
    getMovieTypeStore : function() {
		return Ext.create('Ext.data.Store', {
			fields : [ 'name' ],
			data : [ {
				name : "动作"
			}, {
				name : "冒险"
			}, {
				name : "喜剧"
			}, {
				name : "爱情"
			}, {
				name : "战争"
			}, {
				name : "恐怖"
			}, {
				name : "武侠"
			}, {
				name : "悬疑"
			}, {
				name : "科幻"
			}, {
				name : "动画"
			}, {
				name : "奇幻"
			}, {
				name : "青春"
			}, {
				name : "励志"
			}, {
				name : "历史"
			}, {
				name : "剧情"
			}, {
				name : "其他"
			} ]
		});
	}
});
