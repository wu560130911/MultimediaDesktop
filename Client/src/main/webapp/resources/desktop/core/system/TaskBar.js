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
 

Ext.define('Wdesktop.core.system.TaskBar', {
    extend: 'Ext.toolbar.Toolbar',

    requires: [
        'Ext.button.Button',
        'Ext.resizer.Splitter',
        'Ext.menu.Menu',

        'Wdesktop.core.system.StartMenu'
    ],

    alias: 'widget.taskbar',

    cls: 'ux-taskbar',

    /**
     * @cfg {String} startBtnText
     * The text for the Start Button.
     */
    startBtnText: '开始',

    initComponent: function () {
        var me = this;

        me.startMenu = new Wdesktop.core.system.StartMenu(me.startConfig);//开始菜单

        me.quickStart = new Ext.toolbar.Toolbar(me.getQuickStart());//快捷启动菜单

        me.windowBar = new Ext.toolbar.Toolbar(me.getWindowBarConfig());//已打开的窗口
        
        me.tray = new Ext.toolbar.Toolbar(me.getTrayConfig());//快捷右方菜单（如：时间）

        me.items = [
            {
                xtype: 'button',
                cls: 'ux-start-button',
                iconCls: 'ux-start-button-icon',
                menu: me.startMenu,
                menuAlign: 'bl-tl',
                text: me.startBtnText
            },
            me.quickStart,
            {
                xtype: 'splitter', html: '&#160;',
                height: 20, width: 3,
                cls: 'x-toolbar-separator x-toolbar-separator-horizontal'
            },
            //'-',
            me.windowBar,
            '-',
            me.tray
        ];

        me.callParent();
    },

    afterLayout: function () {
        var me = this;
        me.callParent();
        me.windowBar.el.on('contextmenu', me.onButtonContextMenu, me);
    },

    /**
     * This method returns the configuration object for the Quick Start toolbar. A derived
     * class can override this method, call the base version to build the config and
     * then modify the returned object before returning it.
     */
    getQuickStart: function () {//快速启动菜单
        var me = this, ret = {
            minWidth: 30,
            width: 100,
            height: 25,
            items: [],
            enableOverflow: true
        };
        Ext.each(me.quickStart, function (item) {
            ret.items.push({
                tooltip: { text: item.name, align: 'bl-tl' },
                overflowText: item.name,
                iconCls: item.iconCls+'_mini',
                module: item.module,
                appId:item.id,
                handler: me.onQuickStartClick,
                scope: me
            });
        });

        return ret;
    },
    
    removeMenuItemApp:function(app){
    	var me = this,items = me.startMenu.menu.items.items,existsItem = null;
    	Ext.each(items,function(item){
    		if(item.module==app.module){
    			existsItem = item;
    			return false;
    		}
    	});
    	if(existsItem){
    		 me.startMenu.menu.remove(existsItem);
    	}
    },
    
    addMenuItemApp:function(data){
    	var app = new Object();
    	Ext.apply(app,data);
    	var me = this,items = me.startMenu.menu.items.items,exists = true;
    	Ext.each(items,function(item){
    		if(item.module==app.module){
    			exists = false;
    			return exists;
    		}
    	});
    	if(exists){
    		//添加快速启动的逻辑，先添加，然后向后台服务器发送添加请求
    		app['appId']=app['id'];
    		app['iconCls']=app['iconCls']+'_mini';
    		app['text'] = app['name'];
    		app['scope'] = me;
    		delete app['id'];
    		me.startMenu.addMenuItem(app);
    		//TODO 这儿不对返回值进行判断，默认操作成功
    		mainApplication.tools.ajaxTool.PostData('userApp/changeAppIcon.json',{
    			type:'2',
    			id:app['appId']
    		});
    	}
    },
    
    removeQuickStart:function(app){
    	var me = this,items = me.quickStart.items.items,existsItem = null;
    	Ext.each(items,function(item){
    		if(item.module==app.module){
    			existsItem = item;
    			return false;
    		}
    	});
    	if(existsItem){
    		me.quickStart.remove(existsItem);
    	}
    },
    
    addQuickStart:function(app){
    	var me = this,items = me.quickStart.items.items,exists = true;
    	Ext.each(items,function(item){
    		if(item.module==app.module){
    			exists = false;
    			return exists;
    		}
    	});
    	if(exists){
    		//添加快速启动的逻辑，先添加，然后向后台服务器发送添加请求
    		me.quickStart.add({ 
    			tooltip: { text: app.name, align: 'bl-tl' },
                overflowText: app.name,
                iconCls: app.iconCls+'_mini',
                module: app.module,
                handler: me.onQuickStartClick,
                appId:app.id,
                scope: me
      		});
    		//TODO 这儿不对返回值进行判断，默认操作成功
    		mainApplication.tools.ajaxTool.PostData('userApp/changeAppIcon.json',{
    			type:'1',
    			id:app.id
    		});
    	}
    },

    /**
     * This method returns the configuration object for the Tray toolbar. A derived
     * class can override this method, call the base version to build the config and
     * then modify the returned object before returning it.
     */
    getTrayConfig: function () {//快捷栏右边属性
        var ret = {
            width: 163,
            items: this.trayItems//时钟
        };
        delete this.trayItems;
        return ret;
    },

    getWindowBarConfig: function () {
        return {
            flex: 1,
            cls: 'ux-desktop-windowbar',
            items: [ '&#160;' ],
            layout: { overflowHandler: 'Scroller' }//也可以设置成Menu
        };
    },

    getWindowBtnFromEl: function (el) {
        var c = this.windowBar.getChildByElement(el);
        return c || null;
    },

    onQuickStartClick: function (btn) {
        var module = btn.module;
        if (module) {
        	this.app.createWindow(module,btn.overflowText);
        }
    },
    
    onButtonContextMenu: function (e) {
        var me = this, t = e.getTarget(), btn = me.getWindowBtnFromEl(t);
        if (btn) {
            e.stopEvent();
            me.windowMenu.theWin = btn.win;
            me.windowMenu.showBy(t);
        }
    },

    onWindowBtnClick: function (btn) {
        var win = btn.win;

        if (win.minimized || win.hidden) {
            win.show();
        } else if (win.active) {
            win.minimize();
        } else {
            win.toFront();
        }
    },

    addTaskButton: function(win) {
        var config = {
            iconCls: win.iconCls,
            enableToggle: true,
            toggleGroup: 'all',
            width: 140,
            margins: '0 2 0 3',
            text: Ext.util.Format.ellipsis(win.title, 20),
            listeners: {
                click: this.onWindowBtnClick,
                scope: this
            },
            win: win
        };

        var cmp = this.windowBar.add(config);
        cmp.toggle(true);
        return cmp;
    },

    removeTaskButton: function (btn) {
        var found=null, me = this;
        me.windowBar.items.each(function (item) {
            if (item === btn) {
                found = item;
            }
            return !found;
        });
        if (found) {
            me.windowBar.remove(found);
        }
        return found;
    },

    setActiveButton: function(btn) {
        if (btn) {
            btn.toggle(true);
        } else {
            this.windowBar.items.each(function (item) {
                if (item.isButton) {
                    item.toggle(false);
                }
            });
        }
    }
});

/**
 * @class Ext.ux.desktop.TrayClock
 * @extends Ext.toolbar.TextItem This class displays a clock on the toolbar.
 */
Ext.define('Wdesktop.core.system.TrayClock', {
	
			extend : 'Ext.toolbar.TextItem',
			alias : 'widget.trayclock',
			timeFormat : 'H:i:s',
			iconCls : 'clock',
			GetWeek : function() {
				var d, day;
				var x = new Array("星期日", "星期一", "星期二");
				x = x.concat("星期三", "星期四", "星期五");
				x = x.concat("星期六");
				d = new Date();
				day = d.getDay();
				return (x[day]);
			},
			GetDate:function(){
				var year,month,day,d;
				d = new Date();
				year = d.getFullYear();
				month = d.getMonth()+1;
				day = d.getDate();
				return year+"/"+month+"/"+day;
			},
			initComponent : function() {
				var me = this;
				me.callParent();
			},
			afterRender : function() {
				var me = this;
				Ext.Function.defer(me.updateTime, 100, me);
				me.callParent();
			},
			onDestroy : function() {
				var me = this;
				if (me.timer) {
					window.clearTimeout(me.timer);
					me.timer = null;
				}
				me.callParent();
			},
			updateTime : function() {

				var me = this, time = me.GetDate()+'&nbsp;'+me.GetWeek() + '&nbsp;'
						+ Ext.Date.format(new Date(), me.timeFormat), text = time;
				if (me.lastText != text) {
					me.setText(text);
					me.lastText = text;
				}
				me.timer = Ext.Function.defer(me.updateTime, 1000, me);
			}
		});
