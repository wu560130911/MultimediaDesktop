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
 
Ext.define('Wdesktop.desktop.widget.PlayList', {
    extend: 'Ext.grid.Panel',
    alias : "widget.playlist",
    requires:[
        'Wdesktop.desktop.store.PlaylistStore',
        'Wdesktop.app.MovieApp.view.MovieInfo'
    ],
    width : 170,
    maxWidth:180,
	split : true,
	title:'播放列表',
	collapsible : true,
	columnLines:true,
	rowLines:true,
	titleAlign:'center',
	loadMask:true,
	id:'video_play_lists',
	multiSelect : true,
	floatable: false,
	loadingText:'列表加载中......',
	iconCls:'media_play_list',
	emptyText:'您还没有收藏影片',
    initComponent : function() {
    	var me = this;
    	
    	me.store = Ext.create('Wdesktop.desktop.store.PlaylistStore');
    	
    	Ext.applyIf(me, { 
    		viewConfig: {
    	        rootVisible: false,
    	        plugins: [
    	          Ext.create('Ext.grid.plugin.DragDrop', {
    	          })
    	        ],
    	        listeners:{
        			render:function(view,eopts){
        		    	view.tip = Ext.create('Ext.tip.ToolTip', {
        		    		target: view.el,
        		    		delegate: view.itemSelector,
        		    		trackMouse: true,
        		    		listeners:{
        		    			beforeshow: function(tip) {
        		    				var movie = view.getRecord(tip.triggerElement).raw;
        		    				tip.update('' + movie.movie.title+'-<font color="green" >'+movie.movie.actor + '</font>');
        		    			}
        		    		}
        		    	});
        		    }
        		}
    	    },
    	    columns:[{
    	    	xtype:'gridcolumn',
    	    	dataIndex : "id",
    	    	flex: 1,
    	    	text: '影片',
    	    	renderer:function(value){
    	    		value = this.getStore().getById(value).raw.movie.title;
    	    		if(value.length>12){
    	    			value = value.substring(0,9)+'...';
    	    		}
    	    		return value;
    	    	}
    	    }],
    		tools:[{
    			type:'close',
    			handler:function(event,toolEl,owner,tool){
    				var treepanel = tool.up('gridpanel');
    				if(treepanel.getCollapsed()==false){
    					treepanel.hide();
    				}
    			}
    		}],
    		listeners:{
    			containercontextmenu:function(treepanel,e,eopts){
    				e.stopEvent();
    			},
    			itemcontextmenu:function(treeview,record,item,index,e,eopts){
    				e.stopEvent();
					me.videoIndex=index;
    				me.record = record;
    				me.tip.showAt(e.getXY());
    			}
    		},
    		bbar:[{ 
    			xtype: 'button', 
    			text: '刷新',
    			iconCls:'x-tbar-loading',
    			handler:function(btn){
    				btn.up('gridpanel').getStore().reload();
    			}
    		}]
    	});
    	me.tip = Ext.create('Ext.menu.Menu',{
    		width: 100,
    		record:'',
    		items:[{
    			text:'播放',
    			iconCls:'media_play',
    			handler:function(btn){
    				me.PlayVideo(me.record,me.videoIndex);
    			}
    		},{
    			text:'详细信息',
    			iconCls:'video_more_info',
    			handler:function(btn){
    				var video = me.record;
    				me.VideoMoreInfo(video);
    			}
    		},{
    			text:'移除',
    			iconCls:'delete',
    			handler:function(btn){
    				
    				ainApplication.tools.ajaxTool.PostData('playlist/movies/delete.json',{
    					mediaId:me.record.data.id
    				},function(response, opts){
    					var data = Ext.JSON.decode(response.responseText);
    					if(data['success']){
    						Ext.MessageBox.alert('提示','删除成功',null,this);
    						me.getStore().remove(me.record);
    					}else{
    						Ext.MessageBox.alert('警告',data['error'],null,this);
    					}
    				});
    			}
    		},{
    			text:'刷新列表',
    			iconCls:'x-tbar-loading',
    			handler:function(){
    				me.getStore().reload();
    			}
    		}]
    	});
    	me.callParent(arguments);
    	//加载数据
		Ext.data.StoreManager.lookup(this.getStore()).load();
    },
    PlayVideo:function(data,index){
    	
    	var videoPanel = mainApplication.desktop.app.createWindow('Wdesktop.desktop.widget.VideoWindow','');

		var video = videoPanel.down('video');	
		video.setVideo(data['raw']['movie'],videoPanel);
		video.videoIndex=index;
    	
    },
    VideoMoreInfo:function(data){
    	
    	var movieInfo = Ext.getCmp('wms_movie_info');
    	if(movieInfo == null){
    		var wmsdesktop = mainApplication.desktop;
    		movieInfo = wmsdesktop.createWindow({
                id: 'wms_movie_info',
                title: '影片详细信息',
                iconCls: 'wms_movie_add',
                hideMode: 'offsets',
                closeAction:"destroy",
                layout:"fit",
                items:{xtype: 'movieinfo',moviedata:data['raw']['movie']}
            });
    	}
    	movieInfo.setTitle(data['raw']['movie'].title);
    	movieInfo.down('movieinfo').getForm().setValues(data['raw']['movie']);
    	movieInfo.show();
    }
});
