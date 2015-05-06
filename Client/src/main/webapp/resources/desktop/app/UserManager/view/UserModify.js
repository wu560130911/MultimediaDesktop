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
 
Ext.define('Wdesktop.app.UserManager.view.UserModify', {
	extend : 'Ext.form.Panel',

	alias : "widget.userModify",

	requires : [ 'Ext.form.field.Text', 'Ext.button.Button' ],

	height : 416,
	width : 423,
	bodyPadding : 10,
	closable : true,
	title : '修改用户姓名',
	method : 'post',
	url : 'userManager/modify.json',
	timeout : 100000,
	waitTitle : '正在提交中...',

	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			items : [ {
				xtype : 'textfield',
				anchor : '100%',
				fieldLabel : '账号',
				name : 'id',
				readOnly : true
			}, {
				xtype : 'textfield',
				anchor : '100%',
				fieldLabel : '姓名',
				name : 'name'
			}, {
				xtype : 'textfield',
				anchor : '100%',
				fieldLabel : '绑定邮箱',
				name : 'email',
				readOnly : true
			}, {
				xtype : 'textfield',
				anchor : '100%',
				fieldLabel : '角色',
				name : 'role',
				readOnly : true
			}, {
				xtype : 'textfield',
				anchor : '100%',
				fieldLabel : '密码',
				inputType : 'password',
				name : 'password',
				allowBlank : false
			}, {
				xtype : 'button',
				anchor : '40%',
				height : 36,
				margin : '20 20 20 20',
				width : 76,
				text : '修改姓名',
				handler : me.onFormSubmitClick
			}, {
				xtype : 'button',
				anchor : '40%',
				height : 39,
				margin : '20 20 20 20',
				width : 79,
				text : '重置',
				handler : me.onFormDataLoad
			} ]
		});

		me.callParent(arguments);
		me.onFormDataLoad();
	},

	onFormDataLoad : function() {
		var me = Ext.ComponentQuery.query('userModify')[0];
		if(!me){
			me = this;
		}
		mainApplication.tools.ajaxTool.PostAsyncData(
				'userManager/userInfo.json', {}, function(response, options) {
					var data = Ext.JSON.decode(response.responseText);
					if (data['success']) {
						me.getForm().setValues(data['data']);
					} else {
						Ext.MessageBox.alert('警告', data['error'], null, this);
					}
				});
	},
	onFormSubmitClick : function() {
		var me = Ext.ComponentQuery.query('userModify')[0];
		var form = me.getForm();
		if (form.isValid()) {
			
			form.submit({
				waitMsg : '表单提交中',
				success : function(form, action) {
					Ext.Msg.show({
						title : '系统提示',
						msg : '修改成功',
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
