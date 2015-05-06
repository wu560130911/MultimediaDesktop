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
 
Ext.define('Wdesktop.app.UserManager.view.UserInfo', {
	extend : 'Ext.form.Panel',
	
	alias : "widget.userInfo",

	requires : [ 'Ext.form.field.Text', 'Ext.form.CheckboxGroup',
			'Ext.form.field.Checkbox' ],

	height : 416,
	width : 423,
	bodyPadding : 10,
	closable : true,
	title : '个人信息',

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
				name : 'name',
				readOnly : true
			}, {
				xtype : 'textfield',
				anchor : '100%',
				fieldLabel : '绑定邮箱',
				name : 'email',
				readOnly : true
			}, {
				xtype : 'textfield',
				anchor : '100%',
				fieldLabel : '积分',
				name : 'credit',
				readOnly : true
			}, {
				xtype : 'textfield',
				anchor : '100%',
				fieldLabel : '注册时间',
				name : 'registerTime',
				readOnly : true
			}, {
				xtype : 'textfield',
				anchor : '100%',
				fieldLabel : '最后登录时间',
				name : 'lastLoginTime',
				readOnly : true
			}, {
				xtype : 'checkboxgroup',
				fieldLabel : '状态',
				items : [ {
					xtype : 'checkboxfield',
					name : 'disable',
					boxLabel : '锁定',
					readOnly : true
				}, {
					xtype : 'checkboxfield',
					name : 'vStatus',
					boxLabel : '激活',
					readOnly : true
				} ]
			}, {
				xtype : 'textfield',
				anchor : '100%',
				fieldLabel : '角色',
				name : 'role',
				readOnly : true
			} ]
		});

		me.callParent(arguments);
		
		mainApplication.tools.ajaxTool.PostAsyncData(
				'userManager/userInfo.json', {}, function(response, options) {
					var data = Ext.JSON.decode(response.responseText);
					if (data['success']) {
						me.getForm().setValues(data['data']);
					} else {
						Ext.MessageBox.alert('警告', data['error'], null, this);
					}
				});
	}
});
