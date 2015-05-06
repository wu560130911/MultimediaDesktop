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
 
Ext.define('Wdesktop.desktop.widget.AudioPlayerWindow', {
	extend : 'Wdesktop.core.system.Module',

	uses : [ 'Wdesktop.core.widget.AudioPlayer' ],

	id : 'audioPlayerWindow',

	init : function() {
	},

	createWindow : function() {
		var me = this, desktop = me.app.getDesktop(), win = desktop
				.getWindow(me.id);
		if (!win) {
			win = desktop.createWindow({
				id : me.id,
				title : '音乐播放器2',
				width : 410,
				height : 250,
				iconCls : 'music_main_mini',
				border : false,
				maximizable : false,
				autoScroll: true,
				layout : 'fit',
				defaults : {
					labelWidth : 80
				},
				listeners : {
					beforeclose : function() {

					}
				},
				items : [ {
					xtype : 'audioPlayer'
				} ]
			});
		}
		win.show();
		return win;
	}

});
