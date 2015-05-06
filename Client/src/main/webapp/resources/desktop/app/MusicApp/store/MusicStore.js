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
 
Ext.define("Wdesktop.app.MusicApp.store.MusicStore", {
	extend : "Ext.data.Store",
	requires : [ 'Wdesktop.app.MusicApp.model.MusicModel',
			'Ext.data.proxy.Ajax', 'Ext.data.reader.Json' ],

	constructor : function(cfg) {
		var me = this;
		cfg = cfg || {};
		me.callParent([ Ext.apply({
			autoLoad : false,
			model : 'Wdesktop.app.MusicApp.model.MusicModel',
			proxy : {
				type : "ajax",
				url : "media/music/list.json",
				reader : {
					type : "json",
					root : "musics"
				}
			},
		}, cfg) ]);
	}
});
