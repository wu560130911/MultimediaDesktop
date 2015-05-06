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
 
Ext
		.define(
				'Wdesktop.desktop.widget.WallpaperManager',
				{
					extend : 'Ext.panel.Panel',
					alias : 'widget.wallpaperManager',

					requires : [ 'Ext.tab.Panel', 'Ext.tab.Tab',
							'Ext.toolbar.Paging',
							'Wdesktop.desktop.widget.WallpaperView',
							'Wdesktop.desktop.widget.AddWallpaper',
							'Wdesktop.core.theme.ThemeView',
					        'Wdesktop.core.theme.ThemeModel'],

					height : 412,
					width : 541,
					layout : 'border',
					header : false,

					initComponent : function() {
						var me = this;
						me.store = Ext.create('Ext.data.Store', {
							autoLoad : true,
							fields : [ 'path' ],
							proxy : {
								type : 'ajax',
								url : 'wallpaper/list.json',
								reader : {
									type : 'json',
									root : 'wallpapers'
								}
							},
							listeners : {
								load : function() {
								}
							}
						});
						me.comboboxStore = Ext.create('Ext.data.Store', {
							fields : [ 'name' ],
							data : [ {
								"name" : "风景"
							}, {
								"name" : "创意"
							}, {
								"name" : "摄影"
							}, {
								"name" : "影视"
							}, {
								"name" : "高清"
							}, {
								"name" : "其他"
							} ]
						});
						Ext
								.applyIf(
										me,
										{
											items : [
													{
														xtype : 'tabpanel',
														region : 'center',
														items : [ {
															xtype : 'panel',
															layout : 'fit',
															title : '壁纸列表',
															items : [ {
																xtype : 'wallpaperView',
																store : me.store
															} ],
															dockedItems : [
																	{
																		xtype : 'pagingtoolbar',
																		dock : 'bottom',
																		width : 360,
																		store : me.store,
																		displayInfo : true,
																		items : [{
																			xtype: 'checkboxfield',
																			boxLabel:'拉伸',
																			checked:comm.get('wallpaper_stretch'),
														                    inputValue: 'true'
														                },{
																			xtype : 'button',
																			scale:'medium',
																			text : '设为壁纸',
																			//iconCls:'button_file_open',
																			handler:me.setWallpaper
																		}]
																	},
																	{
																		xtype : 'toolbar',
																		dock : 'top',
																		defaults : {
																			labelWidth : 40
																		},
																		items : [
																				{
																					xtype : 'combobox',
																					width : 171,
																					fieldLabel : '类型',
																					labelWidth : 40,
																					store : me.comboboxStore,
																					queryMode : 'local',
																					displayField : 'name',
																					name : 'type',
																					valueField : 'name'
																				},
																				{
																					xtype : 'datefield',
																					width : 167,
																					name : 'start',
																					fieldLabel : '开始',
																					validator : function(
																							value) {
																						var me = this;
																						var date = me
																								.up(
																										"toolbar")
																								.query(
																										"datefield");
																						if (date[1]
																								.getValue() == null
																								|| date[0]
																										.getValue() < date[1]
																										.getValue()) {
																							if (date[1]
																									.hasActiveError()) {
																								date[1]
																										.isValid();
																							}
																							return true;
																						}
																						return "结束时间必须大于开始时间";
																					},
																					maxValue : new Date()
																				},
																				{
																					xtype : 'datefield',
																					width : 167,
																					name : 'end',
																					fieldLabel : '结束',
																					validator : function(
																							value) {
																						var me = this;
																						var date = me
																								.up(
																										"toolbar")
																								.query(
																										"datefield");
																						if (date[1]
																								.getValue() == null
																								|| date[0]
																										.getValue() < date[1]
																										.getValue()) {
																							if (date[0]
																									.hasActiveError()) {
																								date[0]
																										.isValid();
																							}
																							return true;
																						}
																						return "结束时间必须大于开始时间";
																					},
																					maxValue : new Date()
																				},
																				{
																					xtype : 'button',
																					width : 60,
																					text : '搜索',
																					handler : me.onSearchClick
																				},
																				{
																					xtype : 'button',
																					width : 70,
																					text : '清空搜索',
																					handler : me.onClearClick
																				} ]
																	} ]
														},{
											        		title : '主题',
											        		layout : 'fit',
											        		items : [me.createThemeView()]
											        	},
														Ext
														.create(
																'Wdesktop.desktop.widget.AddWallpaper',
																{
																	comboboxStore : me.comboboxStore
																})  ]
													}]
										});
						me.callParent(arguments);
					},
					onSearchClick : function() {
						var me = this;
						var combobox = me.up("toolbar").query("combobox")[0];
						var datefield = me.up("toolbar").query("datefield");
						if (combobox.isValid() && datefield[0].isValid()
								&& datefield[1].isValid()) {
							var comboboxValue = combobox.getValue();
							var startValue = datefield[0].getValue();
							var endValue = datefield[1].getValue();
							var data = {};
							if (comboboxValue != null) {
								data['type'] = comboboxValue;
							}
							if (startValue != null) {
								data['startDate'] = Ext.Date.format(new Date(
										startValue), 'Y-m-d');
							}
							if (endValue != null) {
								data['endDate'] = Ext.Date.format(new Date(
										endValue), 'Y-m-d');
							}
							me.up("wallpaperManager").store.getProxy().extraParams = data;
							me.up("wallpaperManager").store.loadPage(1);
						}
					},
					onClearClick : function() {
						var me = this;
						me.up("toolbar").query("combobox")[0].clearValue();
						var datefield = me.up("toolbar").query("datefield");
						datefield[0].setValue('');
						datefield[1].setValue('');
						me.up("wallpaperManager").store.getProxy().extraParams = {};
						me.up("wallpaperManager").store.loadPage(1);
					},
					setWallpaper:function(){
						var me = this;
						var wallpaperView = me.up('panel').down('wallpaperView');
						var checkValue = me.up('pagingtoolbar').down('checkboxfield').checked;
						var selectednodes = wallpaperView.getSelectedNodes();
						if(selectednodes.length==0){
							Ext.Msg.alert("提示","请选择您喜欢的壁纸");
							return;
						}
						var record = wallpaperView.getRecord(selectednodes[0]);
						var app = Ext.getCmp('wallpaperManager').app;
						if(comm.get("wallpaper_id")!=record.data.id||comm.get('wallpaper_stretch')==checkValue){
							app.desktop.setWallpaper(record.data.path, checkValue);
							app.desktop.refreshView();
							comm.add('wallpaper_path', record.data.path);
							comm.add('wallpaper_id', record.data.id);
							comm.add('wallpaper_stretch', checkValue);
							app.tools.ajaxTool.PostData('wallpaper/change.json',record.data,function(response){
								var json = Ext.decode(response.responseText);
								if(json['success']!=true){
									Ext.Msg.alert("提示",json['error']);
								}
							});
						}
					},
					createThemeStore : function(){
				    	var me = this;
				    	function child(name,theme,background){
				    		return {name : name,path : theme,imgPath :background };
				    	}
				    	me.themeStore = Ext.create('Ext.data.Store', {
				                model: 'Wdesktop.core.theme.ThemeModel',
				                data: [child('默认风格','default','resources/resources/theme/default.png'),
						               child('现代风格','access','resources/resources/theme/access.png'),
				                       child('银灰风格','gray','resources/resources/theme/gray.png'),
				                       child('海蓝风格','neptune','resources/resources/theme/theme.png')
				                       ]
				            });
				        return me.themeStore;
				    },
				    
				    createThemeView : function(){
				    	var me = this;
				    	me.themeView = new Wdesktop.core.theme.ThemeView({
				    		store : me.createThemeStore(),
				    		listeners : {
				    			itemclick : me.onThemeItemClick,
				    			afterrender: { fn: me.onThemeViewRender, delay: 100 },
				    			scope : me
				    		}
				    	});
				    	
				    	return me.themeView;
				    },
				    onThemeItemClick : function(view,record){
				    	var app = Ext.getCmp('wallpaperManager').app;
				    	var theme = app.desktop.temp_theme;
				    	if(theme !== record.data.path){
				    		Ext.util.CSS.swapStyleSheet('theme','resources/resources/extjs/resources/css/theme-'+record.data.path+'.css');
				    		app.desktop.temp_theme = record.data.path;
				    	}
				    },
				    
				    
				    onThemeViewRender:function(){
				    	var me = this;
				    	var app = Ext.getCmp('wallpaperManager').app;
				    	if(app.desktop.theme == undefined){
				    		app.desktop.theme = theme;
				    		app.desktop.temp_theme = theme;
				    	}
				    	var t = app.desktop.theme;
				    	if (t) {
				            var record = me.themeStore.findRecord('id',t);
				            if(record){
				            	me.themeView.getSelectionModel().select(me.themeStore.indexOf(record));
				            }
				        }
				    }

				});
