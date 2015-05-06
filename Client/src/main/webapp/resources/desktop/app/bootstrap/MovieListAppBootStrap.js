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
 
Ext.define('Wdesktop.app.bootstrap.MovieListAppBootStrap', {
    extend: 'Wdesktop.core.system.Module',

    id: 'wms_movie_list',
    title:'电影列表',
    
    init: function () {
        var me = this;
        if(Wdesktop.app.MovieApp==undefined||Wdesktop.app.MovieApp.controller==undefined){
        	Ext.application({
                name: 'Wdesktop.app.MovieApp', //应用的名字
                appFolder: "resources/desktop/app/MovieApp", //应用的目录
                appProperty: 'MovieApp',
                //当前页面加载完成执行的函数 
                launch:function(){
                },
                controllers: [
                    'MovieController'
    		    ]
            });
    		Ext.create('Wdesktop.core.util.WindowTip',{
    			title:'提示',
    			autoHide:5,
    			html : '<b>'+me.title+'</b>正在加载中...'
    		}).show();
        }
        
        Ext.onReady(function(){
    		return me.createNewWindow();
    	});
        
    },
    
    createNewWindow:function(){
    	var me = this;
        var desktop = me.app.getDesktop();
        var win = desktop.getWindow(me.id);
        if(!win){
        	win = desktop.createWindow({
        		id: me.id,
                title: me.title,
                width: 750,
                height: 520,
                iconCls: 'video_list_desktop_mini',
                border: false,
                hideMode: 'offsets',
                closable:true,
                closeAction:"destroy",
                layout:"fit",
                items:{xtype: 'movielist'}
            });
        	win.show();
        	return win;
        }
        return win;
    },
    
    createWindow: function () {
        var me = this;
        var desktop = me.app.getDesktop();
        var win = desktop.getWindow(me.id);
        
        if(!win){
        	return me.init();
        }
        return win;
    }
});
