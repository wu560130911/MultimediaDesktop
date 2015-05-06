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
 
/**
 * @author WMS
 * @description 本类是Desktop的关键类，主要负责Desktop的初始化等工作
 */
Ext.define('Wdesktop.core.interface.DesktopInterface', {
	
	/**
	 * 混合时间处理类
	 */
    mixins: {
        observable: 'Ext.util.Observable'
    },

    /**
     * 本类需要引用的类
     */
    requires: [
        'Ext.container.Viewport',
        'Wdesktop.core.system.DesktopView',
        'Wdesktop.core.util.AjaxGetData'
    ],
    
    isReady: false,
    modules: null,
    useQuickTips: true,
	appManagerModules:'Wdesktop.app.bootstrap.AppManagerBootStrap',

    constructor: function (config) {
        var me = this;
        me.addEvents({
            'ready':true,
            'beforeunload':true,
            'moduleactioncomplete': true
        });

        me.mixins.observable.constructor.call(this, config);

        if (Ext.isReady) {
            Ext.Function.defer(me.init, 10, me);
        } else {
            Ext.onReady(me.init, me);
        }
    },

    init: function() {
        var me = this, desktopCfg;
        me.tools = {};
        //初始化系统工具
        me.tools.ajaxTool = new Wdesktop.core.util.AjaxGetData();
        
        if (me.useQuickTips) {
            Ext.QuickTips.init();
        }

        me.modules = me.getModules();
        if (me.modules) {
            me.initModules(me.modules);
        }
        
        desktopCfg = me.getDesktopConfig();
        Ext.MessageBox.updateProgress(0.25,'25%','<br/>正在初始化桌面...');
        
        me.desktop = new Wdesktop.core.system.DesktopView(desktopCfg);

        me.viewport = new Ext.container.Viewport({
            layout: 'fit',
            items: [ me.desktop ],
            listeners : {
            	'afterrender' : function(){
            		me.desktop.initView();
            		var costTime = (new Date().getTime()) - startLoadingTime;
            		Ext.MessageBox.updateProgress(1,'100%','<br/>桌面加载完成,耗时:' + costTime + 'ms');
            	}
            }
        });

        Ext.getBody().on('keydown', function(e){
        	//var me = this;
        	if(e.getKey() == e.ESC){
        		me.onLogout();
        	}
        	else if(e.getKey() == e.F5){
        		e.stopEvent();
        		Ext.Msg.confirm('系统提示', '您确定要刷新页面么?',function(btn){
        			if(btn == 'yes'){
        				window.location.reload();
        			}
        		});
        	}
        	else if(e.getKey() == e.F1){
        		e.stopEvent();
        		Ext.Msg.alert('系统提示', '查看帮助!');
        	}
        	else if(e.getKey() == e.F3){
        		e.stopEvent();
        		me.createWindow(me.appManagerModules,'应用管理');
        	}
        },me);
        
        //监听一个事件
        Ext.EventManager.on(window, 'beforeunload', me.onUnload, me);

        me.isReady = true;
        //触发一个ready事件
        me.fireEvent('ready', me);
    },

    /**
     * 初始化桌面的配置，被方法将在子类中覆盖，并由子类调用
     * 
     */
    getDesktopConfig: function () {
        var me = this, cfg = {
            app: me,
            taskbarConfig: me.getTaskbarConfig()
        };

        Ext.apply(cfg, me.desktopConfig);
        return cfg;
    },

    getModules: Ext.emptyFn,

    /**
     * 开始界面，菜单
     * 
     * @method initModules
     * 
     */
    getStartConfig: function () {
        var me = this,
            cfg = {
                app: me,
                menu: []
            };

        Ext.apply(cfg, me.startConfig);

        Ext.each(me.modules, function (module) {
        	
        	//可以直接这样做的原因是，之前一步已经为其做了处理函数
        	cfg.menu.push(module);
        	
            /*launcher = module.launcher;
            if (launcher) {
            	//创建一个函数，作为菜单启动的处理函数
                launcher.handler = launcher.handler || Ext.bind(me.createWindow, me, [module]);
                cfg.menu.push(module.launcher);
            }*/
        });

        return cfg;
    },

    /**
     * 处理开始菜单栏的点击事件
     */
    createWindow: function(module,text) {
    	var me = this;
        var win = Ext.create(module,{app : me}).createWindow();
        if (win) {
            me.desktop.restoreWindow(win);
        }
        return win;
    },

    /**
     *获取系统状态栏的参数，主要包含：菜单栏、快速启动栏、时间显示 
     *其中 startConfig是菜单栏
     *quickStart是快速启动栏
     *trayItems是右边的状态栏，本系统只包含时钟
     */
    getTaskbarConfig: function () {
        var me = this, cfg = {
            app: me,
            startConfig: me.getStartConfig()
        };

        Ext.apply(cfg, me.taskbarConfig);
        return cfg;
    },

    initModules : function(modules) {
        var me = this;
        Ext.each(modules, function (module) {
            module.app = me;
            module.handler = function(){
            	me.createWindow(module.module,module.text);
            };
        });
    },

    getModule : function(name) {
    	var ms = this.modules;
        for (var i = 0, len = ms.length; i < len; i++) {
            var m = ms[i];
           // console.log(m);
            if (m.id == name || m.appType == name ||m.module==name) {
                return m;
            }
        }
        return null;
    },

    onReady : function(fn, scope) {
        if (this.isReady) {
            fn.call(scope, this);
        } else {
            this.on({
                ready: fn,
                scope: scope,
                single: true
            });
        }
    },

    getDesktop : function() {
        return this.desktop;
    },

    onUnload : function(e) {
        if (this.fireEvent('beforeunload', this) === false) {
            e.stopEvent();
        }
    }
});
