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
 
Ext.define('Wdesktop.core.widget.AudioPlayer', {
	extend : 'Ext.panel.Panel',

	alias : 'widget.audioPlayer',
	layout : 'fit',
	html : '<div id="audio_player_gerald"></div>',
	autoScroll: true,

	initComponent : function() {
		this.callParent();
	},

	afterRender : function() {
		var me = this;

		me.divAudio = document.getElementById('audio_player_gerald');

		 me.el.on('contextmenu', function (e) {
	            e.stopEvent();
	        }, me);
		
		WMSDesktop.Bootstrap.loadRequires([{
			type : WMSDesktop.Bootstrap.resTypes.JAVASCRIPT,
			url : 'resources/resources/js/player-with-css.min.js'
		}]);
		
		WMSDesktop.Bootstrap.loadCallback=function(){
			Ext.onReady(function(){
				me.audio = new Player({
				    container: me.divAudio,
				    image: 'resources/resources/image/a0ad718d86d21262ccd6ff271ece08a3.png'
				});
				
				mainApplication.tools.ajaxTool.PostData('playlist/musics/list.json',{},function(response, opts){
		    		 var obj = Ext.JSON.decode(response.responseText);
						if (obj['success']) {
							if(obj['datas']!=null){
								var data = [];
								Ext.Array.each(obj['datas'],function(model){
									data.push({
										"name":model['music']['title'],
										"url":'files/musics/'+model['music']['filename']
									});
								});
								if(data.length==0){
									me.audio.setSongs([{
										"name":"当你老了",
										"url":"http://m5.file.xiami.com/17/2017/2024265879/1774005550_16345485_l.mp3?auth_key=20b402019841c260d8c67c307d200810-1430092800-0-null"
									}]);
								}else{
									me.audio.setSongs(data);
								}
								me.audio.play(0);
							}
						}
		    	 });
			});
		};
	},

	afterComponentLayout : function() {
		var me = this;
		me.callParent(arguments);
	},

	onDestroy : function() {
		var me = this;
		var audio = me.audio.audio;
        if (audio&& audio.pause) {
        	audio.pause();
            audio.remove();
            delete audio;
        }
		delete me.audio;
		me.divAudio.remove();
		me.callParent();
	}
});
