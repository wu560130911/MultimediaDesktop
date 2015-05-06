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
 
Ext.define('Wdesktop.core.widget.Video', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.video',
    layout: 'fit',
    autoplay: false,
    controls: true,
    bodyStyle: 'background-color:#000;color:#fff',
    html: '',
    suggestChromeFrame: true,
	changeTimeLength:10,
	videoIndex:-1,
    
    initComponent: function () {
    	var me = this;
    	me.contextmenu = Ext.create('Ext.menu.Menu',{
    		width: 100,
    		items:[{
    			text:'隐藏列表',
    			handler:function(btn){
    				var video_play_lists = Ext.getCmp('video_play_lists');
    				if(video_play_lists.isHidden()){
    					video_play_lists.show();
    					btn.setText('隐藏列表');
    	            }else{
    	            	video_play_lists.hide();
    	            	btn.setText('显示列表');
    	            }
    			}
    		},{
    			text:'播放',
				iconCls:'fa fa-play-circle',
    			disabled:true,
    			handler:function(){
    				var video_play = document.getElementById('video_play');
    				if(video_play.paused){
    					video_play.play();
    				}
    			}
    		},{
    			text:'隐藏控制条',
    			disabled:false,
    			handler:function(btn){
    				var video_play = document.getElementById('video_play');
    				if(video_play.controls){
    					video_play.controls = false;
    					btn.setText('显示控制条');
    				}else{
    					video_play.controls = true;
    					btn.setText('隐藏控制条');
    				}
    			}
    		},{
    			xtype: 'menucheckitem',
    			text:'静音',
    			disabled:false,
    			handler:function(btn){
    				var video_play = document.getElementById('video_play');
    				btn.setChecked(!video_play.muted);
    				video_play.muted = btn.checked;
    				btn.up('menu').hide();
    			}
    		},{
    			text:'关于我',
    			handler:function(){
    				
    			}
    		}]
    	});
    	
    	me.callParent();
    },
    
    afterRender: function () {
    	var me = this;
    	me.callParent(arguments);
        var fallback;

        if (this.fallbackHTML) {
            fallback = this.fallbackHTML;
        } else {
        	fallback = "您的浏览器不支持HTML5的Video.";
            if ( Ext.isIE && this.suggestChromeFrame ) {
                /* chromeframe requires that your site have a special tag in the header
                 * see http://code.google.com/chrome/chromeframe/ for details
                 */
                fallback += '<a href="http://www.google.com/chromeframe"'
                     + '>Get Google Chrome Frame for IE</a>';
            } else if ( Ext.isChrome ) {
                fallback += '<a href="http://www.google.com/chrome"'
                     + '>升级谷歌浏览器</a>';
            } else if ( Ext.isGecko ) {
                fallback += '<a href="http://www.mozilla.com/en-US/firefox/upgrade.html"'
                     + '>升级到 Firefox 3.5</a>';
            } else {
                fallback += '<a href="http://www.mozilla.com/en-US/firefox/upgrade.html"'
                     + '>获取 Firefox 3.5</a>';
            }
        }

        // match the video size to the panel dimensions
        var size = me.getSize();

        var cfg = Ext.copyTo({
            tag   : 'video',
            width : size.width,
            id:'video_play',
            height: size.height
        },
        me, 'poster,start,loopstart,loopend,playcount,autobuffer,loop');

        // just having the params exist enables them
        if (me.autoplay) {
            cfg.autoplay = 1;
        }
        if (me.controls) {
            cfg.controls = 1;
        }

        // handle multiple sources
        if (Ext.isArray(me.src)) {
            cfg.children = [];

            for (var i = 0, len = me.src.length; i < len; i++) {
                if (!Ext.isObject(me.src[i])) {
                	 throw "source list passed to html5video panel must be an array of objects";
                }

                cfg.children.push(
                    Ext.applyIf({tag: 'source'}, me.src[i])
                );
            }

            cfg.children.push({
                html: fallback
            });

        } else {
            cfg.src  = me.src;
            cfg.html = fallback;
        }

        me.video = me.body.createChild(cfg);
        var el = me.video.dom;
        this.supported = (el && el.tagName.toLowerCase() == 'video');
        
        me.el.on('contextmenu', function (e) {
            e.stopEvent();
            me.contextmenu.showAt(e.getXY());
        }, me);
        
        me.el.on('click',function(e){
        	var video_play = document.getElementById('video_play');
        	if(video_play.error==null&&(video_play.networkState==1||video_play.networkState==2)){
        		if(video_play.paused){
            		video_play.play();
            	}else{
            		video_play.pause();
            	}
        	}
        },me);
        
        me.contextmenu.on('beforeshow',function(){
        	var btn = me.contextmenu.items.get(0);
        	if(me.up('window').down('playlist').isHidden()){
        		btn.setText('显示列表');
        	}else{
        		btn.setText('隐藏列表');
        	}
        	var video_play = document.getElementById('video_play');
        	var canPlay = true;
        	if(video_play.error==null&&(video_play.networkState==1||video_play.networkState==2)&&video_play.paused){
        		canPlay = false;
        	}
        	var playButton = me.contextmenu.items.get(1);
        	playButton.setDisabled(canPlay);
        	
        	var controls = me.contextmenu.items.get(2);
        	if(video_play.controls){
        		controls.setText('隐藏控制条');
        	}else{
        		controls.setText('显示控制条');
        	}
        	var volnumNone = me.contextmenu.items.get(3);
        	volnumNone.setChecked(video_play.muted==true);
			
        },me);
        
		if(!me.keyMap){
			me.keyMap = Ext.create('Ext.util.KeyMap', {
				target: me.up('window').id,
				binding: [{//快进
					key: Ext.EventObject.RIGHT,
					fn: function(){
						//console.log(me.keyMap.isEnabled());
						var video_play = document.getElementById('video_play');
						if(video_play.error==null&&(video_play.networkState==1||video_play.networkState==2)){
							var currentTime = video_play.currentTime;
							if(currentTime+me.changeTimeLength>video_play.duration){
								video_play.currentTime=video_play.duration;
							}else{
								video_play.currentTime=currentTime+me.changeTimeLength;
							}
						}					
					},
					scope:me
				}, {//快退
					key: Ext.EventObject.LEFT,
					fn: function(){
						//console.log(me.keyMap.isEnabled());
						var video_play = document.getElementById('video_play');
						if(video_play.error==null&&(video_play.networkState==1||video_play.networkState==2)){
							var currentTime = video_play.currentTime;
							if(currentTime-me.changeTimeLength<0){
								video_play.currentTime=0;
							}else{
								video_play.currentTime=currentTime-me.changeTimeLength;
							}
						}
					},
					scope:me
				}, {//暂停
					key: Ext.EventObject.SPACE,
					fn: function(){
						//console.log(me.keyMap.isEnabled());
						var video_play = document.getElementById('video_play');
						if(video_play.error==null&&(video_play.networkState==1||video_play.networkState==2)){
							if(video_play.paused){
								video_play.play();
							}else{
								video_play.pause();
							}
						}
					},
					scope:me
				}]
			});
		}
		
		
		//这儿也可以利用事件，对视频进行分片，达到更好的观看效果
		//播放结束
		Ext.get('video_play').on('ended', function(e,value) {
			if(me.videoIndex==null||me.videoIndex<0){
				Ext.newstip.msg('系统提示', '视频播放结束!',3000);
				return;
			}
			var playlist = me.up('window').down('playlist');
			var store = playlist.store;
			var count = store.getCount();
			me.videoIndex++;
			var currentIndex = me.videoIndex;
			if(count<=0||count<=me.videoIndex){
				Ext.newstip.msg('系统提示', '收藏列表的视频播放完毕',3000);
				return;
			}
			var data = store.getAt(currentIndex);
			if(data!=null){
				me.setVideo(data['raw']['movie'],me.up('window'));
				me.videoIndex = currentIndex;
				Ext.newstip.msg('提示', '即将为您播放<b>'+data['raw']['movie']['title']+'</b>',3000);
			}
		});
		
        /*Ext.get('video_play').on('click', function() {
    		var video_play = document.getElementById('video_play');
    		if(video_play.paused){
    			video_play.play();
    		}else{
    			video_play.pause();
    		}
        });
        
        Ext.get('video_play').on('dblclick', function() {
        	
        });
        
        Ext.get('video_play').on('ended', function() {
        });*/
        
    },

    afterComponentLayout : function() {
        var me = this;

        me.callParent(arguments);

        if (me.video) {
            me.video.setSize(me.body.getSize());
        }
    },

    onDestroy: function () {
		var me = this;
        var video = this.video;
        if (video) {
            var videoDom = video.dom;
            if (videoDom && videoDom.pause) {
                videoDom.pause();
            }
            video.remove();
            delete video;
            //this.video = null;
        }
		me.keyMap.destroy();
        this.callParent();
    },
    setVideo:function(data,videoPanel){
    	var me = this;
		me.videoIndex=-1;
    	var old_video_player = document.getElementById('video_play');
    	//old_video_player.parentNode.removeChild(old_video_player);
    	old_video_player.src='files/movies/'+data.filename;
    	/*me.src = [{
			src: 'files/movies/'+data.data.filename
		}];
    	
    	me.update();
    	me.afterRender();
		var video_player = document.getElementById('video_play');*/
		var reg = new RegExp(",", "g");//全部替换
		videoPanel.setTitle(data.title+'-'+data.actor.replace(reg,' '));
		if(old_video_player.paused){
			old_video_player.play();
		}
		//videoPanel.videoEl = old_video_player;
		videoPanel.toFront();
    }
});
