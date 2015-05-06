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
 
var menu = new Ext.menu.Menu();
menu.add({
	text:'QQ账号',
	iconCls:'qqlogin',
	handler:function(){
		window.location.href="user_qqLoginPage.action";
	}
});
Ext.define('Wdesktop.desktop.widget.LoginWindow', {
	extend: 'Ext.window.Window',
	height: 308,
	hidden: false,
	id:'login',
	width: 429,
	modal : true,
	closable: false,
	resizable:false,
	draggable:false,
	title: '<div style="font-size:18px;">系统登录</div>',
	titleAlign: 'center',
	expandOnShow: false,
	
	initComponent: function() {
    	var me = this;
    	Ext.applyIf(me, {
    		items: [{
                xtype: 'form',
                id : "loginform",
                height: 273,
                frame:false,
                defaults: {
                    labelWidth: 40
                },
                layout: {
                    type: 'absolute'
                },
                bodyPadding: 10,
                items: [{
                        xtype: 'textfield',
                        x: 160,
                        y: 40,
                        id: 'userid',
                        width: 220,
                        name: 'userid',
                        fieldLabel: '账号',
                        allowBlank: false,
                        emptyText: '请输入您的账号',
                        maxLength: 19,
                        minLength: 6,
                        vtype: 'alphanum'
                    },{
                        xtype: 'textfield',
                        x: 160,
                        y: 90,
                        id: 'password',
                        width: 220,
                        inputType: 'password',
                        name: 'password',
                        fieldLabel: '密码',
                        emptyText: '请输入您的密码',
                        allowBlank: false,
                        maxLength: 20,
                        minLength: 7
                    },{
                        xtype: 'displayfield',
                        x: 0,
                        y: -3,
                        height: 271,
                        width: 150,
                        value: "<img src='js/MyDesktop/wallpapers/login.png' />",
                        fieldLabel: ''
                    },{
                    	xtype : 'textfield',
                    	x : 160,
    					y : 140,
    					width : 125,
    					name:'vcode',
    					id:'vcode',
    					labelWidth : 50,
    					allowBlank : false,
    					blankText : '请输入验证码',
    					emptyText : '验证码',
    					fieldLabel : '验证码'
                    },{
                    	xtype : 'image',
    					x : 300,
    					y : 135,
    					height : 30,
    					width : 80,
    					id:'image',
    					src: 'kaptcha.jpg'
                    },{
                        xtype: 'button',
                        id:'wms_web_login',
                        x: 155,
                        y: 200,
                        height: 30,
                        width: 70,
                        text: '登录',
                        handler:me.onSubmitClick
                    },{
                        xtype: 'button',
                        x: 235,
                        y: 200,
                        height: 30,
                        width: 70,
                        text: '注册',
                        handler:function(button,e){
                    		Ext.getCmp('login').close();
                    		Ext.create('Wdesktop.desktop.widget.Register').show();
                    	}
                    },{
                        xtype: 'splitbutton',
                        x: 320,
                        y: 200,
                        height: 30,
                        width: 90,
                        text: '其他登录',
                        menu:menu/*,
                        handler:function(button,e){
                        	menu.showAt(e.getPoint()); //显示在当前位置 
                    	}*/
                    },{
                        xtype: 'displayfield',
                        x: 140,
                        y: 240,
                        height: 30,
                        id: 'ip',
                        width: 260,
                        value: 'IP:'+returnCitySN.cip +' 地点:'+returnCitySN.cname+"&nbsp;"
                    },{
                    	xtype: 'displayfield',
                    	x: 180,
                    	y: 165,
                    	height: 30,
                    	width: 250,
                    	id:'logininfo',
                    	value:''
                    }]
            }]
    	});
    	me.callParent(arguments);
    	Ext.EventManager.onWindowResize(me.onWindowResize,me);
	},
	
	onWindowResize:function(){
		var me = this;
		me.center();
	},
	
	onShowComplete: function() {
    	var me = this;
    	
    	new Ext.util.KeyMap({
            target: me.getId(),
            processEvent:function(self){
            	self.winId = me.getId();
            	return self;
            },
            binding: [{
                key: Ext.EventObject.ENTER,
                fn: function(key,eventobject,value){
                	me.onSubmitClick(Ext.getCmp(eventobject.winId).query('form > button')[0]);
                }
            }]
        });
    	me.callParent(arguments);// call the superclass onRender method
    },
    onSubmitClick:function(btn){
		var form = btn.ownerCt;
		var basic = form.getForm();
		if(basic.isValid()){
			var userid = Ext.getCmp('userid').getValue();
			var password = Ext.getCmp('password').getValue();
			var pro = Wdesktop.desktop.widget.LoginWindow.Progress();
			Ext.Ajax.request({
				url : "user_login.action",
				method : "POST",
				timeout : 10000,
				params : {
					'user.id' : userid,
					'user.password' : password,
					'user.ip':returnCitySN.cip,
					'vcode':Ext.getCmp('vcode').getValue()
				},
				success : function(response, opts) {
					pro.close();//滚动条消失
					if (Ext.JSON.decode(response.responseText).message == "success") {
						Ext.getCmp('login').close();
						sessionStorage.setItem('user_id',userid);
						sessionStorage.setItem('user_name',Ext.JSON.decode(response.responseText).username);
						sessionStorage.setItem('user_creadit',Ext.JSON.decode(response.responseText).creadit);
						self.location.href="index.jsp";
					}else{	
						Ext.getCmp('loginform').getForm().reset();
						Ext.getCmp('logininfo').setValue('<font color=red size=3 face="华文楷体">'+Ext.JSON.decode(response.responseText).message+'.</font>');
						Ext.getCmp('image').setSrc('kaptcha.jpg?t='+Math.floor(Math.random()*1000));
					}
				},
				failure:function(response,options){
	    			pro.close();//滚动条消失
	    			Ext.Msg.alert('错误','网络错误！server-side failure with status code'+response.status);
	    			basic.reset();
	    		}
			});
		}else{
			Ext.getCmp('logininfo').setValue('<font color=red size=3 face="华文楷体">请按要求输入账号和密码.</font>');
		}
	}
});
Wdesktop.desktop.widget.LoginWindow.Progress = function(){
	return Ext.Msg.show({
	    title: '提示',
	    progressText: '正在登录中，请稍等...',
	    width: 300,
	    wait:true,
	    modal:true,
	    progress:true,
	    prompt:false,
	    closable:false,
	    waitConfig:{
	    	interval: 500, //bar will move fast!
	    	increment: 15,
	    	text: '正在登录中，请稍等...',
	    	scope: this
	    },
	    icon: Ext.window.MessageBox.INFO
	});
};
