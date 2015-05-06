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
 
Ext.define('Wdesktop.app.AppManager.view.AddMyApplication', {
	
	extend : 'Ext.panel.Panel',
	
	alias : "widget.appManagerAddMyApplication",
	
	height : 401,
	width : 606,
	closable : true,
	layout : {
		type : 'fit'
	},
	title : '上传我的应用',

	initComponent : function() {
		var me = this;
		me.comboboxStore = me.getAppTypeStore();
		Ext.applyIf(me, {
			items : [ {
				xtype : 'form',
				bodyPadding : 10,
				header : false,
				method : 'post',
				url : 'app/addApp.json',
				items : [ {
					xtype : 'fieldset',
					height : 135,
					title : '基本信息',
					items : [ {
						xtype : 'textfield',
						anchor : '100%',
						allowBlank : false,
						name:'name',
						fieldLabel : '应用名称'
					}, {
						xtype : 'textfield',
						anchor : '100%',
						allowBlank : false,
						name:'iconCls',
						fieldLabel : '应用样式'
					}, {
						xtype : 'textfield',
						anchor : '100%',
						allowBlank : false,
						name:'module',
						fieldLabel : '应用模块'
					}, {
						xtype : 'textfield',
						anchor : '100%',
						allowBlank : false,
						name:'version',
						fieldLabel : '应用版本'
					} ]
				}, {
					xtype : 'fieldset',
					layout : {
						type : 'table'
					},
					title : '',
					items : [ {
						xtype : 'fieldset',
						anchor : '50%',
						colspan : 2,
						height : 184,
						title : '更多信息',
						toggleOnTitleClick : false,
						items : [ {
							xtype : 'textareafield',
							anchor : '100%',
							height : 99,
							fieldLabel : '描述',
							allowBlank : false,
							name:'description',
							labelAlign : 'top'
						}, {
							xtype : 'combobox',
							anchor : '100%',
							fieldLabel : '应用类型',
							store : me.comboboxStore,
							queryMode : 'local',
							displayField : 'name',
							name : 'typeGroup',
							valueField : 'name',
							allowBlank : false,
							editable : false,
							repeatTriggerClick : true
						},{
				            xtype      : 'fieldcontainer',
				            fieldLabel : '权限限制',
				            defaultType: 'radiofield',
				            anchor : '100%',
				            allowBlank : false,
				            labelWidth : 60,
				            defaults: {
				                flex: 1
				            },
				            layout: 'hbox',
				            items: [{
									boxLabel : '管理员',
									inputValue: '管理员',
									name      : 'role'
								}, {
									boxLabel : '开发者',
									name      : 'role',
									inputValue: '开发者'
								}, {
									boxLabel : '用户',
									name : 'role',
									inputValue: '用户',
									checked:true
								} 
				            ]
				        } ]
					}, {
						xtype : 'fieldset',
						anchor : '50%',
						colspan : 2,
						autoRender : false,
						height : 180,
						margin : '5 0 5 5',
						width : 246,
						title : '应用表单',
						items : [ {
							xtype : 'textareafield',
							anchor : '100%',
							height : 99,
							fieldLabel : '应用提示',
							allowBlank : false,
							name : 'tip',
							labelAlign : 'top'
						}, {
							xtype : 'button',
							height : 34,
							margin : '0 10 0 0',
							width : 64,
							text : '提交',
							handler:me.onSubmitClick
						}, {
							xtype : 'button',
							height : 34,
							margin : '0 0 0 10',
							width : 64,
							text : '重置',
							handler:me.onResetClick
						} ]
					} ]
				} ]
			} ]
		});

		me.callParent(arguments);
	},
	getAppTypeStore:function(){
		return Ext.create('Ext.data.Store', {
			fields : [ 'name' ],
			data : [ {
				"name" : "文字"
			}, {
				"name" : "管理"
			}, {
				"name" : "休闲"
			}, {
				"name" : "娱乐"
			}, {
				"name" : "游戏"
			}, {
				"name" : "音乐"
			}, {
				"name" : "视频"
			}, {
				"name" : "电影"
			}, {
				"name" : "其他"
			} ]
		});
	},
	onResetClick:function(){
		var me = this;
		me.up('form').getForm().reset();
	},
	onSubmitClick:function(){
		var me = this;
		var form = me.up('form').getForm();
		if(form.isValid()){
			form.submit({
				success : function(form, action) {
					form.reset();
					Ext.Msg.show({
						title : '系统提示',
						msg : '应用上传成功，请等待管理员审核',
						buttons : Ext.Msg.OK,
						closable : false
					});
				},
				failure : function(form, action) {
					Ext.Msg.show({
						title : '错误',
						msg : action.result.error,
						buttons : Ext.Msg.OK,
						closable : false
					});
				}
			});
		}
	}

});
