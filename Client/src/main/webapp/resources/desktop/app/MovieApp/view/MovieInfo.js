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
 
Ext.define('Wdesktop.app.MovieApp.view.MovieInfo', {
	extend : 'Ext.form.Panel',
	alias : "widget.movieinfo",
	requires : [ 'Wdesktop.app.MovieApp.view.MovieImage' ],
	frame : false,
	height : 420,
	width : 659,
	layout : {
		type : 'absolute'
	},
	closeAction : 'destroy',
	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
			defaults : {
				labelWidth : 60
			},
			items : [ {
				xtype : 'fieldset',
				x : 10,
				y : 0,
				height : 220,
				width : 310,
				defaults : {
					labelWidth : 60
				},
				layout : {
					type : 'absolute'
				},
				collapsible : true,
				title : '影片海报',
				items : [ {
					xtype : 'movieimage',
					x : 0,
					y : 0,
					frame : false,
					height : 185,
					width : 285,
					item : [ {
						src : 'resources/resources/wallpapers/desktop.jpg'
					}, {
						src : 'resources/resources/wallpapers/cloud.jpg'
					} ]
				} ]
			}, {
				xtype : 'fieldset',
				x : 0,
				y : 230,
				height : 175,
				defaults : {
					labelWidth : 60
				},
				layout : {
					type : 'absolute'
				},
				collapsible : true,
				autoScroll: true,
				title : '影片描述',
				items : [ {
					xtype : 'displayfield',
					x : 0,
					y : 0,
					height : 135,
					width : 615,
					name : 'description'
				} ]
			}, {
				xtype : 'fieldset',
				x : 340,
				y : 0,
				height : 220,
				defaults : {
					readOnly : true,
					labelWidth : 60
				},
				layout : {
					type : 'absolute'
				},
				collapsible : true,
				title : '影片基本信息',
				items : [ {
					xtype : 'textfield',
					x : 20,
					y : 0,
					width : 250,
					fieldLabel : '影片名称',
					readOnly : true,
					name : 'title'
				}, {
					xtype : 'textfield',
					x : 20,
					y : 30,
					width : 250,
					fieldLabel : '主演',
					readOnly : true,
					name : 'actor'
				}, {
					xtype : 'textfield',
					x : 20,
					y : 60,
					width : 250,
					fieldLabel : '导演',
					readOnly : true,
					name : 'author'
				}, {
					xtype : 'textfield',
					x : 20,
					y : 90,
					width : 250,
					fieldLabel : '出版时间',
					readOnly : true,
					name : 'madetime'
				}, {
					xtype : 'textfield',
					x : 20,
					y : 120,
					width : 250,
					fieldLabel : '影片类型',
					readOnly : true,
					name : 'type'
				}, {
					xtype : 'textfield',
					x : 20,
					y : 120,
					width : 250,
					readOnly : true,
					hidden : true,
					name : 'filename'
				}, {
					xtype : 'button',
					x : 130,
					y : 150,
					height : 30,
					width : 70,
					text : '点播',
					handler:function(btn){
						var data = btn.up('movieinfo').moviedata;
						var videoPanel = mainApplication.desktop.app.createWindow('Wdesktop.desktop.widget.VideoWindow','');
						var video = videoPanel.down('video');
						video.setVideo(data,videoPanel);
					}
				} ]
			} ]
		});
		me.callParent(arguments);
	}
});
