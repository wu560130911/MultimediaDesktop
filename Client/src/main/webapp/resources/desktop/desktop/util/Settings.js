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
 

Ext.define('Wdesktop.desktop.util.Settings', {
    extend: 'Ext.window.Window',

    uses: [
        'Ext.tree.Panel',
        'Ext.tree.View',
        'Ext.form.field.Checkbox',
        'Ext.layout.container.Anchor',
        'Ext.layout.container.Border',
        'Wdesktop.core.system.Wallpaper',
        'Wdesktop.desktop.model.WallpaperModel',
        'Wdesktop.core.theme.ThemeView',
        'Wdesktop.core.theme.ThemeModel'
    ],

    layout: 'fit',
    title: '个性化',
    modal: true,
    width: 640,
    height: 480,
    border: false,

    initComponent: function () {
        var me = this;
        me.selected = me.desktop.getWallpaper();
        me.stretch = me.desktop.wallpaper.stretch;

        me.preview = Ext.create('widget.wallpaper');//调用extjs中core中的壁纸模板类  
        me.preview.setWallpaper(me.selected);
        me.tree = me.createTree();

        me.buttons = [
            { text: '确定', handler: me.onOK, scope: me },
            { text: '取消', handler: me.close, scope: me }
        ];

        me.items = [{
        	xtype : 'tabpanel',
        	layout : 'fit',
			minTabWidth : 100,
        	items : [{
        		title : '主题',
        		layout : 'fit',
        		items : [me.createThemeView()]
        	},{
        		layout: 'anchor',
        	    title: '壁纸设置',
        	    items:[{
                    anchor: '0 -30',
                    border: false,
                    layout: 'border',
                    items: [me.tree,{
                            xtype: 'panel',
                            title: '预览',
                            region: 'center',
                            layout: 'fit',
                            items: [ me.preview ]
                    }]
            },{
                    xtype: 'checkbox',
                    boxLabel: '拉伸适合屏幕',
                    checked: me.stretch,
                    listeners: {
                        change: function (comp) {
                            me.stretch = comp.checked;
                        }
                    }
            }]
        	}]
        }];
        
        me.callParent();
    },

    createThemeStore : function(){
    	var me = this;
    	function child(name,theme,background){
    		return {name : name,theme : theme,background :background };
    	}
    	me.themeStore = Ext.create('Ext.data.Store', {
                model: 'Wdesktop.core.theme.ThemeModel',
                data: [/*child('默认风格','default','default.png'),
		               child('现代风格','access','access.png'),
                       child('银灰风格','gray','gray.png'),
                       child('银灰风格','neptune','gray.png')*/
                       ]
            });
    	/**
    	 * 向服务器请求数据
    	 */
    	Ext.Ajax.request({
    		async:false,
    		url : "theme/themeaction_listTheme.action",
			method : "POST",
			timeout : 10000,
			
			success:function(response, opts){
				var obj= Ext.JSON.decode(response.responseText);
				if (obj.message == "success") {
					if(obj.themes!=null){
						me.themeStore.add(obj.themes);
					}
				}else{
					self.location.href = 'login.jsp';
				}
			}
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
    
    createTree : function() {
        var me = this;
        function child (img) {
            return { img: img, text: me.getTextOfWallpaper(img), iconCls: '', leaf: true };
        }

        var tree = new Ext.tree.Panel({
            title: '壁纸',
            rootVisible: false,
            lines: false,
            autoScroll: true,
            width: 150,
            region: 'west',
            split: true,
            minWidth: 100,
            listeners: {
                afterrender: { fn: this.setInitialSelection, delay: 100 },
                select: this.onSelect,
                scope: this
            },
            store: new Ext.data.TreeStore({
                model: 'Wdesktop.desktop.model.WallpaperModel', //初始化壁纸
                root: {
                    text:'Wallpaper',
                    expanded: true,
                    children:[
                        { text: "无", iconCls: '', leaf: true },
                        {text:'山水风景',img:'19201080.jpg',icon:'js/MyDesktop/png/19201080.png',leaf:true},
                        {text:'晴空行云',img:'blue-curtain.jpg',icon:'js/MyDesktop/png/blue-curtain.png',leaf:true},
                        {text:'青山绿草',img:'blue.jpg',icon:'js/MyDesktop/png/blue.png',leaf:true},
                        {text:'梦幻世界',img:'cloud.jpg',icon:'js/MyDesktop/png/cloud.png',leaf:true},
                        {text:'奇特世界',img:'colorado-farm.jpg',icon:'js/MyDesktop/png/colorado-farm.png',leaf:true},
                        {text:'大象',img:'desktop2.jpg',icon:'js/MyDesktop/png/desktop2.png',leaf:true},
                        {text:'蒙古草原',img:'FGHJ_079020.jpg',icon:'js/MyDesktop/png/FGHJ_079020.png',leaf:true},
                        {text:'小麦',img:'fields-of-peace.jpg',icon:'js/MyDesktop/png/fields-of-peace.png',leaf:true},
                        {text:'清新早晨',img:'fresh-morning.jpg',icon:'js/MyDesktop/png/fresh-morning.png',leaf:true},
                        {text:'青草风景',img:'landscape.jpg',icon:'js/MyDesktop/png/landscape.png',leaf:true},
                        {text:'绿水青山',img:'mountains.jpg',icon:'js/MyDesktop/png/mountains.png',leaf:true},
                        {text:'天空',img:'sky.jpg',icon:'js/MyDesktop/png/sky.png',leaf:true},
                        {text:'幻彩蓝天',img:'pinky_light.jpg',icon:'js/MyDesktop/png/pinky_light.png',leaf:true},
                        {text:'梦翔天际',img:'desktop.jpg',icon:'js/MyDesktop/png/desktop.png',leaf:true}
                    ]
                }
            })
        });

        return tree;
    },

    getTextOfWallpaper: function (path) {
        var text = path, slash = path.lastIndexOf('/');
        if (slash >= 0) {
            text = text.substring(slash+1);
        }
        var dot = text.lastIndexOf('.');
        text = Ext.String.capitalize(text.substring(0, dot));
        text = text.replace(/[-]/g, ' ');
        return text;
    },

    onOK: function () {
        var me = this;
        if (me.selected) {
            me.desktop.setWallpaper(me.selected, me.stretch);
        }
        localStorage.setItem('wallpaper_path',me.selected);
        localStorage.setItem('wallpaper_stretch',me.stretch);
        if(me.desktop.temp_theme !== me.desktop.theme){
        	Ext.Ajax.request({
				url : "user_theme.action",
				method : "POST",
				timeout : 10000,
				params : {
					'user.theme.id' : me.desktop.temp_theme
				},
				success : function(response, opts) {
					if (Ext.JSON.decode(response.responseText).message == "success") {
						me.desktop.theme=me.desktop.temp_theme;
					}
				}
			});
        }
        me.destroy();
    },

    onSelect: function (tree, record) {
        var me = this;

        if (record.data.img) {
            me.selected = 'js/MyDesktop/wallpapers/' + record.data.img;//指定壁纸的路径
        } else {
            me.selected = Ext.BLANK_IMAGE_URL;
        }

        me.preview.setWallpaper(me.selected);
    },

    setInitialSelection: function () {
        var s = this.desktop.getWallpaper();
        if (s) {
            var path = 'js/MyDesktop/Wallpaper/' + this.getTextOfWallpaper(s);
            this.tree.selectPath(path, 'text');
        }
    },
    
    onThemeItemClick : function(view,record){
    	var me = this,theme = me.desktop.temp_theme;
    	if(theme !== record.data.theme){
    		Ext.util.CSS.swapStyleSheet('theme',record.data.path);
    		me.desktop.temp_theme = record.data.id;
    	}
    },
    
    
    onThemeViewRender:function(){
    	var me = this;
    	if(me.desktop.theme == undefined){
    		me.desktop.theme = theme;
    		me.desktop.temp_theme = theme;
    	}
    	var t = me.desktop.theme;
    	if (t) {
            var record = me.themeStore.findRecord('id',t);
            if(record){
            	me.themeView.getSelectionModel().select(me.themeStore.indexOf(record));
            }
        }
    }
});
