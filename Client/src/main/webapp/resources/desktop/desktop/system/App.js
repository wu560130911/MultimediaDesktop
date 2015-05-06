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
 
var desktopapp = Ext.define('Wdesktop.desktop.system.App', {
    extend: 'Wdesktop.core.interface.DesktopInterface',
    
    id:'desktopapp',

    /*加载子模块的app入口文件*/
    requires: [
        'Wdesktop.core.model.ShortcutModel',
        'Wdesktop.desktop.widget.SystemStatus',
        'Wdesktop.desktop.util.Settings',
        'Wdesktop.module.WallpaperModule'
    ],

    /*初始化函数*/
    init: function() {
    	var me = this;
        me.callParent();
        mainApplication = me;
        
    },

    /*将子模块的app文件放到desktop的管理器*/
    getModules : function(){//开始菜单
    	var me = this,data = me.loadAsynData('userApp/getAppStartMenu.json');
    	Ext.each(data, function (item) {
    		item['scope']=me;
    		item['text'] = item['name'];
    		item['appId']=item['id'];
    		item['iconCls']=item['iconCls']+'_mini';
    		delete item['id'];
        });
    	return data;
    },

    /*配置桌面化*/
    getDesktopConfig: function () {
        var me = this, ret = me.callParent();

        return Ext.apply(ret, {
            //cls: 'ux-desktop-black',
             //鼠标右键菜单
            contextMenuItems: [{ 
            	text: '个性化', 
                handler: me.onSettings, //响应函数
                scope: me 
            }],
           //桌面快捷方式
            shortcuts: me.getShortcutsStore(),
            //背景
            wallpaper: comm.get('wallpaper_path'),
            //背景图是否伸展
            wallpaperStretch: comm.get('wallpaper_stretch')
        });
    },

    getShortcutsStore:function(){
    	var me = this;
    	var store = Ext.create('Ext.data.Store', {
            model: 'Wdesktop.core.model.ShortcutModel',
            data: [
            ]
        });
    	me.loadShortcutsData(store);
    	return store;
    },
    
    loadShortcutsData:function(store){
    	var me = this;
    	me.loadData(store,'userApp/getAppDesktop.json');
    },
    
    // config for the start menu
    // 开始菜单右边
    getStartConfig : function() {//开始菜单的设置（右边导航栏）
        var me = this, ret = me.callParent();
        
        return Ext.apply(ret, {
            title: '<font size=3 face="华文楷体" color="#FFFFFF">'+comm.get('user_name')+'</font>',
            iconCls: 'user',
            id:'start_menu',
            height: 300,
            toolConfig: {
                width: 100,
                items: [
                    {
                        text:'个性化',
                        iconCls:'settings',
                        handler: me.onSettings,
                        scope: me
                    },{
                        text:'锁定系统',
                        iconCls:'lock',
                        handler: me.onLock,
                        scope: me
                    },'-',
                    {
                        text:'退出系统',
                        iconCls:'logout',
                        handler: me.onLogout,
                        scope: me
                    }
                ]
            }
        });
    },
    //任务栏设置
    getTaskbarConfig: function () {
        var me = this,ret = me.callParent(),data=me.loadAsynData('userApp/getAppQuickStart.json');
        return Ext.apply(ret, {
        	//快速启动栏
            quickStart: data,
            trayItems: [{
            	xtype: 'trayclock', flex: 1 //这个是快捷栏右边的时间组件，是定义在TaskBar.js中的Ext.ux.desktop.TrayClock类。  
            }]
        });
    },
    
    loadAsynData:function(url){
    	var me = this,data=null;
   	 	me.tools.ajaxTool.PostAsyncData(url,{},function(response, opts){
   		 var obj = Ext.JSON.decode(response.responseText);
				if (obj['success']) {
					if(obj['apps']!=null){
						data = obj['apps'];
					}
				}else{
					self.location.href="user/login";
				}
   	 });
   	 	return data;
   },
    
    loadData:function(store,url){
    	var me = this;
    	 me.tools.ajaxTool.PostData(url,{},function(response, opts){
    		 var obj = Ext.JSON.decode(response.responseText);
				if (obj['success']) {
					if(obj['apps']!=null){
						store.add(obj['apps']);
					}
				}else{
					self.location.href="user/login";
				}
    	 });
    },

    onLogout: function () {//退出系统的操作
        Ext.Msg.confirm('退出系统', '确定要注销吗？',function(btn,text){
        	if(btn=='yes'){
        		self.location.href = 'user/logout';
        	}
        });
    },

    onSettings: function () {//设置壁纸的操作
    	var me = this;
    	var wallpaper = new Wdesktop.module.WallpaperModule({
    		app: me
    	});
		wallpaper.createWindow();
    },
    onLock:function(){
    	Ext.Loader.loadScript({
    		url:'js/MyDesktop/DesktopApp/LockWindow.js',
    		onLoad:function(){
    			var lockWin = new MyDesktop.DesktopApp.LockWindow({
    	    		desktop: this.desktop
    	    	});
    	    	lockWin.show();
    		},
    		onError:function(){
    			Ext.Msg.show({
    				title: '错误',
    			    msg: '网络连接错误，无法动态加载文件',
    			    icon: Ext.window.MessageBox.ERROR,
    			    buttons: Ext.Msg.OK
    			});
    		}
    	});
    }
});
