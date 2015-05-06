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
 
Ext.define('Wdesktop.desktop.widget.Register', {
	
    extend: 'Ext.window.Window',
    draggable: false,
    frame: false,
    height: 380,
    hidden: false,
    resizable:false,
    id: 'register',
    modal : true,
    width: 520,
    layout: {
        type: 'absolute'
    },
    closable: false,
    title: '<div style="font-size:18px;">用户注册</div>',
    titleAlign: 'center',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [{
            	xtype : "form",
            	id : "registerform",
            	layout: {
            		type: 'absolute'
            	},
            	defaults: {
                	labelWidth: 40,
                	width: 210
            	},
            	height: 360,
            	width: 520,
            	frame:false,
            	items:[{
                    xtype: 'textfield',
                    x: 10,
                    y: 20,
                    id: 'userid',
                    name: 'userid',
                    fieldLabel: '账号',
                    allowBlank: false,
                    blankText: '账号必填',
                    emptyText: '请输入账号',
                    maxLength: 20,
                    minLength: 6,
                    vtype: 'alphanum'
                },
                {
                    xtype: 'textfield',
                    x: 10,
                    y: 60,
                    id: 'password',
                    inputType: 'password',
                    name: 'password',
                    fieldLabel: '密码',
                    allowBlank: false,
                    maxLength: 20,
                    minLength: 7
                },
                {
                    xtype: 'textfield',
                    x: 260,
                    y: 20,
                    id: 'name',
                    name: 'name',
                    fieldLabel: '姓名',
                    allowBlank: false,
                    emptyText: '请输入姓名',
                    width: 240,
                    maxLength: 20,
                    minLength: 2
                },
                {
                    xtype: 'textfield',
                    x: 10,
                    y: 100,
                    id: 'address',
                    name: 'address',
                    fieldLabel: '地址',
                    allowBlank: false,
                    emptyText: '请输入地址',
                    maxLength: 50,
                    minLength: 5
                },
                {
                    xtype: 'datefield',
                    x: 10,
                    y: 140,
                    id: 'birthday',
                    name: 'birthday',
                    fieldLabel: '生日',
                    emptyText: '请输入生日',
                    enableKeyEvents: true,
                    altFormats: 'Y,m,d|Y.m.d|Y/m/d|Y-m-d',
                    format: 'Y年m月d日'
                },
                {
                    xtype: 'htmleditor',
                    anchor: '',
                    x: 260,
                    y: 50,
                    autoShow: false,
                    frame: true,
                    height: 190,
                    id: 'description',
                    style: 'background-color: white;',
                    width: 240,
                    defaultLinkValue: ' ',
                    defaultValue: '介绍下您自己吧！',
                    enableAlignments: false,
                    enableFormat: false,
                    enableLinks: false,
                    enableLists: false,
                    name: 'description',
                    fieldLabel: '自我介绍',
                    hideEmptyLabel: false,
                    labelAlign: 'top',
                    labelSeparator: ':',
//                    labelStyle: 'background-color: #DFE8F6',
                    labelWidth: 60
                },
                {
                    xtype: 'textfield',
                    x: 10,
                    y: 180,
                    id: 'email',
                    name: 'email',
                    fieldLabel: 'E-mail',
                    allowBlank: false,
                    emptyText: '请输入E-mail',
                    enableKeyEvents: true,
                    maxLength: 30,
                    minLength: 5,
                    vtype: 'email'
                },
                {
                    xtype: 'textfield',
                    x: 10,
                    y: 220,
                    id: 'qq',
                    name: 'qq',
                    fieldLabel: 'QQ',
                    emptyText: '请输入QQ',
                    maxLength: 20,
                    minLength: 5,
                    regex: /[1-9][0-9]{4,}/
                },{
                	xtype: 'displayfield',
                	x: 200,
                	y: 305,
                	id:'ip',
                	width: 310,
                	value: 'IP: '+returnCitySN.cip +' 地点:'+returnCitySN.cname+"&nbsp;"
                },{
                    xtype: 'button',
                    x: 176,
                    y: 270,
                    height: 30,
                    width: 60,
                    text: '注册',
                    handler:function(btn){
                		var form = btn.ownerCt;
                		var basic = form.getForm();
                		if(basic.isValid()){
                			var userid = Ext.getCmp('userid').getValue();
                			var password = Ext.getCmp('password').getValue();
                			var name = Ext.getCmp('name').getValue();
                			var address = Ext.getCmp('address').getValue();
                			var birthday = Ext.getCmp('birthday').getValue();
                			var description = Ext.getCmp('description').getValue();
                			var email = Ext.getCmp('email').getValue();
                			var qq = Ext.getCmp('qq').getValue();
                			
                			var pro = Wdesktop.desktop.widget.LoginWindow.Progress();
                			
                			Ext.Ajax.request({
                				url : "user_register.action",
                				method : "POST",
                				timeout : 50000,
                				params : {
									'user.id' : userid,
									'user.password' : password,
									'user.name':name,
									'user.birthplace':address,
									'user.email':email,
									'user.birthday':birthday,
									'user.qq':qq,
									'user.ip':returnCitySN.cip,
									'user.description':description
								},
								success : function(response, opts) {
									pro.close();//滚动条消失
									if (Ext.JSON.decode(response.responseText).message == "success") {
										Ext.getCmp('register').close();
										Ext.create('Wdesktop.desktop.widget.LoginWindow').show();
										Ext.Msg.alert("提示",'验证邮件已经发送到您的邮箱，请验证');
									}else{								
										Ext.Msg.alert("提示",Ext.JSON.decode(response.responseText).message);
									}
								}
                			});
                		}
                	}
                },
                {
                    xtype: 'button',
                    x: 266,
                    y: 270,
                    height: 30,
                    width: 60,
                    text: '登录',
                    handler:function(){
                		Ext.getCmp('register').close();
                		Ext.create('Wdesktop.desktop.widget.LoginWindow').show();
                		Ext.get('image').on('click',function(){
                 			Ext.getCmp('image').setSrc('kaptcha.jpg?t='+Math.floor(Math.random()*1000));
                 		});
                	}
                }]
            }]
        });

        me.callParent(arguments);
        
        Ext.EventManager.onWindowResize(me.onWindowResize,me);
    },
    onWindowResize:function(){
		var me = this;
		me.center();
	}

});
