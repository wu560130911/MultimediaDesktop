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
 
Ext.define('Wdesktop.app.AppManager.view.InstallPanel', {
    extend: 'Ext.window.Window',

    requires: [
        'Ext.form.field.TextArea',
        'Ext.form.CheckboxGroup',
        'Ext.form.field.Checkbox',
        'Ext.button.Button'
    ],

    height: 331,
    width: 529,
    layout: 'absolute',
    closable: true,
    title: '安装应用程序',
    modal: true,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'textfield',
                    x: 30,
                    y: 20,
                    height: 30,
                    width: 200,
                    readOnly: true,
                    size: 23,
                    value:me.appName+"("+me.appVersion+")"
                },
                {
                    xtype: 'textfield',
                    x: 270,
                    y: 20,
                    width: 240,
                    fieldLabel: '开发者',
                    labelWidth: 50,
                    readOnly: true,
                    value:me.userId
                },
                {
                    xtype: 'textareafield',
                    x: 30,
                    y: 60,
                    height: 117,
                    width: 480,
                    readOnly: true,
                    fieldLabel: '应用描述',
                    labelAlign: 'top',
                    value:me.appDescription
                },
                {
                    xtype: 'checkboxgroup',
                    x: 20,
                    y: 210,
                    width: 530,
                    fieldLabel: '',
                    items: [
                        {
                            xtype: 'checkboxfield',
                            value: true,
                            boxLabel: '桌面快捷方式',
                            checked: true
                        },
                        {
                            xtype: 'checkboxfield',
                            boxLabel: '快速启动栏'
                        },
                        {
                            xtype: 'checkboxfield',
                            boxLabel: '开始菜单栏'
                        }
                    ]
                },
                {
                    xtype: 'button',
                    x: 190,
                    y: 250,
                    height: 30,
                    width: 80,
                    text: '安装应用',
                    appId:me.appId,
                    handler:me.onInstallButtonClick
                }
            ]
        });

        me.callParent(arguments);
    },
    onInstallButtonClick:function(){
    	var me = this,windowPanel=me.up('window');
    	var checkboxfields = windowPanel.query('checkboxfield');
    	var appName = windowPanel.appName+"("+windowPanel.appVersion+")";
    	var app = windowPanel.app;
    	var data = {};
    	data['desktopIcon']=checkboxfields[0].checked;
    	data['quickStart']=checkboxfields[1].checked;
    	data['startMenu']=checkboxfields[2].checked;
    	data['id']=me.appId;
    	me.up('window').close();
    	var win = Ext.create('Wdesktop.core.util.WindowTip',{
			title:'提示',
			autoHide:5,
			html : '<b>'+appName+'</b>正在安装中...'
		}).show();
    	win.clearListeners();
    	
    	Ext.Ajax.request({
			url : 'userApp/addApp.json',
			method : "POST",
			timeout : 10000,
			params : data,
			success : function(response,options){
				var data = Ext.JSON.decode(response.responseText);
				if(data['success']==true){
					var win = Ext.create('Wdesktop.core.util.WindowTip',{
		    			title:'提示',
		    			autoHide:5,
		    			html : '<b>'+appName+'</b>安装完成'
		    		}).show();
					win.clearListeners();
					mainApplication.desktop.refreshView();
					if(checkboxfields[1].checked) mainApplication.desktop.taskbar.addQuickStart(app);
					if(checkboxfields[2].checked) mainApplication.desktop.taskbar.addMenuItemApp(app);
					
				}else{
					var win = Ext.create('Wdesktop.core.util.WindowTip',{
		    			title:'提示',
		    			autoHide:5,
		    			html : '<b>'+appName+'</b>安装失败<br>'+data['error']
		    		}).show();
					win.clearListeners();
				}
			}
		});
    }

});
