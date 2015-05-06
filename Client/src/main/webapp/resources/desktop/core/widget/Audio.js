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
 

Ext.define('Wdesktop.core.widget.Audio', {
    extend: 'Ext.panel.Panel',

    alias: 'widget.audio',
    layout: 'fit',
    autoplay: true,
    controls: true,
    bodyStyle: 'background-color:#000000;color:#FFFFFF',
    html: '',
    suggestChromeFrame: false,
    islast:false,

    initComponent: function () {
        this.callParent();
    },

    afterRender: function () {
    	var me = this;
        var fallback;
        if (me.fallbackHTML) {
            fallback = me.fallbackHTML;
        } else {
        	fallback = "您的浏览器不支持HTML5的Audio.";
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
        
        var cfg = Ext.copyTo({
            tag   : 'audio',
            id:'audio_player'
        },
        me, 'start,loopend,autobuffer,loop');

        if (me.autoplay) {
            cfg.autoplay = 1;
        }
        if (me.controls) {
            cfg.controls = 1;
        }
        
        if (Ext.isArray(me.src)) {
            cfg.children = [];
            for (var i = 0, len = me.src.length; i < len; i++) {
                if (!Ext.isObject(me.src[i])) {
                	 throw "source list passed to html5 audio panel must be an array of objects";
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
        if(Ext.isIE){
        	var audio_play_setting = Ext.getCmp('audio');
        	audio_play_setting.setWidth(420);
        	audio_play_setting.setHeight(160);
        }
        me.audio = me.body.createChild(cfg);
        var el = me.audio.dom;
        me.supported = (el && el.tagName.toLowerCase() == 'audio');
        me.el.on('contextmenu', function (e) {
            e.stopEvent();
        }, me);
        Ext.get('audio_player').on('ended', function(e,value) {
        	//currentTime当前播放的位置，赋值可改变位置
        	/*currentSrc
        	currentTime
        	videoWidth
        	videoHeight
        	duration
        	ended
        	error
        	paused
        	muted
        	seeking
        	volume
        	paused
        	muted*/
        });
    },

    afterComponentLayout : function() {
        var me = this;
        me.callParent(arguments);
        if (me.audio) {
        	if(Ext.isIE){
        		me.audio.setWidth(400);
            }else{
            	me.audio.setWidth(300);
            	me.audio.setHeight(30);
            }
        }
    },

    onDestroy: function () {
        var audio = this.audio;
        if (audio) {
            var audioDom = audio.dom;
            if (audioDom && audioDom.pause) {
            	audioDom.pause();
            }
            audio.remove();
            delete audio;
        }
        this.callParent();
    },
    setAudio:function(data,audioPanel){
    	//var me = this;
    	var audio_player = document.getElementById('audio_player');
    	//old_video_player.parentNode.removeChild(old_video_player);
    	audio_player.src='files/musics/'+data.filename;
    	/*me.src = [{
			src: 'files/movies/'+data.data.filename
		}];
    	
    	me.update();
    	me.afterRender();
		var video_player = document.getElementById('video_play');*/
		//var reg = new RegExp(",", "g");//全部替换
		//audioPanel.setTitle(data.title+'-'+data.singer.replace(reg,' '));
		if(audio_player.paused){
			audio_player.play();
		}
		var displayfields = audioPanel.query('displayfield');
		displayfields[0].setValue('<font size=3 face="华文楷体" color=red>'+data.title+'</font>');
		displayfields[1].setValue('<font size=3 face="华文楷体" color=red>'+data.singer+'</font>');
		
		//audioPanel.toFront();
    }
});
