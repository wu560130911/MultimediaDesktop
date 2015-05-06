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
 
Ext.define('Wdesktop.app.MusicApp.controller.MusicController', {
    extend: 'Ext.app.Controller',
    init: function () {
    	var me = this;
    	
    	me.musicListContainercontextmenu = me.getContainercontextmenu();
    	me.musicListItemcontextmenu =  me.getItemcontextmenu();
    	
    	me.checkMusicListContainercontextmenu = me.getCheckItemcontextmenu();
    	me.checkMusicListItemcontextmenu =  me.getCheckContainercontextmenu();
    	
    	me.getGridObj=function(button){
			return button.ownerCt.ownerCt;
		};
		me.control({
	        "musiclist button[text=播放]":{
	        	 click:function(){
		        		me.onAddPlaylistButtonClick(Ext.ComponentQuery.query('musiclist')[0]);
		        	}
	        },'musiclist':{
	        	containercontextmenu:function(panel,e,opts){
	        		e.stopEvent();
	        		me.musicListContainercontextmenu.showAt(e.getXY());
	        	},
	        	itemcontextmenu:function(panel,record,item,index,e,opts){
	        		e.stopEvent();
	        		me.musicListItemcontextmenu.showAt(e.getXY());
	        	},
	        	itemdblclick:me.onPlayButtonClick
	        },'menu[itemId=musiclist] menuitem[text=播放音乐]':{
	        	click:me.onPlayButtonClick
	        },'menu[itemId=musiclist] menuitem[text=添加收藏]':{
	        	click:function(){
	        		me.onAddPlaylistButtonClick(Ext.ComponentQuery.query('musiclist')[0]);
	        	}
	        },'checkMusicList':{
	        	containercontextmenu:function(panel,e,opts){
	        		e.stopEvent();
	        		me.checkMusicListContainercontextmenu.showAt(e.getXY());
	        	},
	        	itemcontextmenu:function(panel,record,item,index,e,opts){
	        		e.stopEvent();
	        		me.checkMusicListItemcontextmenu.showAt(e.getXY());
	        	},
	        	itemdblclick:me.onPlayButtonClick
	        },'menu[itemId=checkMusicList] menuitem[text=审核通过]':{
	        	click:me.onCheckButtonClick
	        },'menu[itemId=checkMusicList] menuitem[text=播放音乐]':{
	        	click:function(){
	        		me.onAddPlaylistButtonClick(Ext.ComponentQuery.query('checkMusicList')[0]);
	        	}
	        }
        });
    },

    onCheckButtonClick:function(){
    	var gridpanel = Ext.ComponentQuery.query('checkMusicList')[0];
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
		
		mainApplication.tools.ajaxTool.PostJsonData('admin/media/checkMusics.json',"["+data.join(",")+"]",function(response,options){
			var data = Ext.JSON.decode(response.responseText);
			if(data['success']){
				gridpanel.getStore().remove(videos);
			}else{
				Ext.MessageBox.alert('警告',data['error'],null,this);
			}
		});
    },
    
    onAddPlaylistButtonClick:function(gridpanel){
    	var audios = gridpanel.getSelectionModel().getSelection();
		if(audios.length <=0){
			Ext.MessageBox.alert('警告','请先选择添加收藏的音乐.',null,this);
			return;
		}
		var data = [];
		
		Ext.Array.each(audios,function(model){
			var app = {'id':model.data.id};
			data.push(Ext.JSON.encode(app));
		});
		
		mainApplication.tools.ajaxTool.PostJsonData('playlist/music/add.json',"["+data.join(",")+"]",function(response,options){
			var data = Ext.JSON.decode(response.responseText);
			if(data['success']){
				Ext.MessageBox.alert('提示','添加成功',null,this);
			}else{
				Ext.MessageBox.alert('警告',data['error'],null,this);
			}
		});
    },
    
    
    onPlayButtonClick:function(){
    	var me = this;
    	var musicGridPanel = Ext.ComponentQuery.query('musiclist')[0];
    	var audios = musicGridPanel.getSelectionModel().getSelection();
		if(audios.length <=0){
			Ext.MessageBox.alert('警告','请先选择电影.',null,this);
			return;
		}
		me.PlayAudio(audios[0]);
    },
    
    PlayAudio:function(data){
    	var audioPanel = mainApplication.desktop.app.createWindow('Wdesktop.desktop.widget.AudioWindow','');

		var audio = audioPanel.down('audio');
		
		audio.setAudio(data.data,audioPanel);
    },
    getItemcontextmenu:function(){
    	var contextmenu = Ext.create('Ext.menu.Menu',{
    		width: 100,
    		itemId: 'musiclist'
    	});
    	contextmenu.add({text:'添加收藏',iconCls:'media_collect'});
    	contextmenu.add({text:'播放音乐',iconCls:'media_play'});
    	return contextmenu;
    },
    getContainercontextmenu:function(){
    	var contextmenu = Ext.create('Ext.menu.Menu',{
    		width: 100,
    		itemId: 'musiclist'
    	});
    	contextmenu.add({text:'添加收藏',iconCls:'media_collect'});
    	return contextmenu;
    },
    
    getCheckItemcontextmenu:function(){
    	var contextmenu = Ext.create('Ext.menu.Menu',{
    		width: 100,
    		itemId: 'checkMusicList'
    	});
    	contextmenu.add({text:'审核通过',iconCls:'media_collect'});
    	contextmenu.add({text:'播放音乐',iconCls:'media_play'});
    	return contextmenu;
    },
    getCheckContainercontextmenu:function(){
    	var contextmenu = Ext.create('Ext.menu.Menu',{
    		width: 100,
    		itemId: 'checkMusicList'
    	});
    	contextmenu.add({text:'审核通过',iconCls:'media_collect'});
    	return contextmenu;
    },
    
    models: [
        'MusicModel'
	],
    stores: [
        
	],
    views: [
    	"MusicAdd","MusicList",'MusicSearch','CheckMusicList'
	]
});
