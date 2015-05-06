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
 
Ext.define('Wdesktop.app.MovieApp.controller.MovieController', {
    extend: 'Ext.app.Controller',
    models: [
       'MovieModel'
    ],
    stores: [
        
    ],
    views: [
        "MovieAdd",'MovieList','MovieInfo','MovieImage','MovieSearch','CheckMovieList'
    ],
    init: function () {
    	var me = this;
    	me.getGridObj=function(button){
			return button.ownerCt.ownerCt;
		};
		me.movieListItemcontextmenu = me.getItemcontextmenu();
		me.movieListContainercontextmenu = me.getContainercontextmenu();
		me.checkMovieListItemcontextmenu = me.getCheckItemcontextmenu();
		me.checkMovieListContainercontextmenu = me.getCheckContainercontextmenu();
		me.control({
        	"movieadd button[text=重置]":{
	        	click:me.MovieAddReset
	        },"movielist button[text=查看详细信息]":{
	        	click:function(){
	        		me.movieMoreInfo(Ext.ComponentQuery.query('movielist')[0]);
	        	}
	        },'menu[itemId=movielist] menuitem[text=添加收藏]':{
	        	click:me.addMoviePlaylist
	        },'movielist':{
	        	containercontextmenu:function(panel,e,opts){
	        		e.stopEvent();
	        		me.movieListContainercontextmenu.showAt(e.getXY());
	        	},
	        	itemcontextmenu:function(panel,record,item,index,e,opts){
	        		e.stopEvent();
	        		me.movieListItemcontextmenu.showAt(e.getXY());
	        	},
	        	itemdblclick:me.onPlayButtonClick
	        },'menu[itemId=movielist] menuitem[text=详细信息]':{
	        	click:function(menu,item,e,eopts){
	        		me.movieMoreInfo(Ext.ComponentQuery.query('movielist')[0]);
	        	}
	        },'menu[itemId=movielist] menuitem[text=播放影片]':{
	        	click:me.onPlayButtonClick
	        },"movielist button[text=播放]":{
	        	click:me.onPlayButtonClick
	        },"checkMovieList":{
	        	containercontextmenu:function(panel,e,opts){
	        		e.stopEvent();
	        		me.checkMovieListContainercontextmenu.showAt(e.getXY());
	        	},
	        	itemcontextmenu:function(panel,record,item,index,e,opts){
	        		e.stopEvent();
	        		me.checkMovieListItemcontextmenu.showAt(e.getXY());
	        	},itemdblclick:function(grid,record,item,index,e,ops){
	        		var videos = grid.getSelectionModel().getSelection();
	        		if(videos.length <=0){
        				Ext.MessageBox.alert('警告','请先选择电影.',null,this);
        				return;
        			}
	        		me.PlayVideo(videos[0]);
	        	}
	        },'menu[itemId=checkMovieList] menuitem[text=<播放>]':{
	        	click:me.onPlayButtonClick2
	        },'menu[itemId=checkMovieList] menuitem[text=查看详细]':{
	        	click:function(menu,item,e,eopts){
	        		me.movieMoreInfo(Ext.ComponentQuery.query('checkMovieList')[0]);
	        	}
	        },'menu[itemId=checkMovieList] menuitem[text=审核通过]':{
	        	click:me.onCheckButtonClick
	        },"checkMovieList button[text=审核通过]":{
	        	click:me.onCheckButtonClick
	        },"movielist button[text=查看详细信息]":{
	        	click:function(){
	        		me.movieMoreInfo(Ext.ComponentQuery.query('checkMovieList')[0]);
	        	}
	        }
        });    
    },
    
    addMoviePlaylist:function(){
    	var gridpanel = Ext.ComponentQuery.query('movielist')[0];
		var videos = gridpanel.getSelectionModel().getSelection();
		if(videos.length <=0){
			Ext.MessageBox.alert('警告','请先选择电影.',null,this);
			return;
		}
		var data = [];
		Ext.Array.each(videos,function(model){
			var app = {'id':model.data.id};
			data.push(Ext.JSON.encode(app));
		});
		mainApplication.tools.ajaxTool.PostJsonData('playlist/movies/add.json',"["+data.join(",")+"]",function(response,options){
			var data = Ext.JSON.decode(response.responseText);
			if(data['success']){
				Ext.MessageBox.alert('提示','添加成功',null,this);
			}else{
				Ext.MessageBox.alert('警告',data['error'],null,this);
			}
		});
    },
    
    onCheckButtonClick:function(){
    	var gridpanel = Ext.ComponentQuery.query('checkMovieList')[0];
    	var videos = gridpanel.getSelectionModel().getSelection();
		if(videos.length <=0){
			Ext.MessageBox.alert('警告','请先选择审核通过的电影.',null,this);
			return;
		}
		var data = [];
		
		Ext.Array.each(videos,function(model){
			var app = {'id':model.data.id};
			data.push(Ext.JSON.encode(app));
		});
		
		mainApplication.tools.ajaxTool.PostJsonData('admin/media/checkMovies.json',"["+data.join(",")+"]",function(response,options){
			var data = Ext.JSON.decode(response.responseText);
			if(data['success']){
				gridpanel.getStore().remove(videos);
			}else{
				Ext.MessageBox.alert('警告',data['error'],null,this);
			}
		});
    },
    
    MovieAddReset:function(btn){
    	btn.up('movieadd').getForm().reset();
    },
    
    onPlayButtonClick:function(){
    	var me = this;
    	var gridpanel = Ext.ComponentQuery.query('movielist')[0];
		var videos = gridpanel.getSelectionModel().getSelection();
		if(videos.length <=0){
			Ext.MessageBox.alert('警告','请先选择电影.',null,this);
			return;
		}
		me.PlayVideo(videos[0]);
    },
    
    onPlayButtonClick2:function(){
    	var me = this;
    	var gridpanel = Ext.ComponentQuery.query('checkMovieList')[0];
		var videos = gridpanel.getSelectionModel().getSelection();
		if(videos.length <=0){
			Ext.MessageBox.alert('警告','请先选择电影.',null,this);
			return;
		}
		me.PlayVideo(videos[0]);
    },
    
    movieMoreInfo:function(gridpanel){
		var videos = gridpanel.getSelectionModel().getSelection();
		if(videos.length <=0){
			Ext.MessageBox.alert('警告','请先选择电影.',null,this);
			return;
		}
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
                items:{xtype: 'movieinfo',moviedata:videos[0].data}
            });
    	}
    	movieInfo.setTitle(videos[0].raw.title);
    	movieInfo.down('movieinfo').loadRecord(videos[0]);
    	movieInfo.show();
    },
    
    
	PlayVideo:function(data){
		
		var videoPanel = mainApplication.desktop.app.createWindow('Wdesktop.desktop.widget.VideoWindow','');

		var video = videoPanel.down('video');
		
		video.setVideo(data.data,videoPanel);
    	
    },
    getItemcontextmenu:function(){
    	var contextmenu = Ext.create('Ext.menu.Menu',{
    		width: 100,
    		itemId: 'movielist'
    	});
    	contextmenu.add({text:'添加收藏',iconCls:'media_collect'});
    	contextmenu.add({text:'播放影片',iconCls:'media_play'},{text:'详细信息',iconCls:'video_more_info'});
    	return contextmenu;
    },
    getContainercontextmenu:function(){
    	var contextmenu = Ext.create('Ext.menu.Menu',{
    		width: 100,
    		itemId: 'movielist'
    	});
    	contextmenu.add({text:'添加收藏',iconCls:'media_collect'});
    	return contextmenu;
    },
    getCheckItemcontextmenu:function(){
    	var contextmenu = Ext.create('Ext.menu.Menu',{
    		width: 100,
    		itemId: 'checkMovieList'
    	});
    	contextmenu.add({text:'审核通过',iconCls:'media_collect'});
    	contextmenu.add({text:'<播放>',iconCls:'media_play'},{text:'查看详细',iconCls:'video_more_info'});
    	return contextmenu;
    },
    getCheckContainercontextmenu:function(){
    	var contextmenu = Ext.create('Ext.menu.Menu',{
    		width: 100,
    		itemId: 'checkMovieList'
    	});
    	contextmenu.add({text:'审核通过',iconCls:'media_collect'});
    	return contextmenu;
    }
});
