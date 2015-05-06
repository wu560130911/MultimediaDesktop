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
 
Ext.define('Wdesktop.core.util.WindowTip', {
    extend: 'Ext.window.Window',
    alias:'windowtip',
    width:226,
    height:160,
    layout:'fit',
    modal : false,
    plain: true,
    shadow:false, //去除阴影 
    draggable:false, //默认不可拖拽 
    resizable:false,
    closable: true,
   // closeAction:'hide', //默认关闭为隐藏  
    autoHide:5, //5秒后自动隐藏，false则不自动隐藏 
    manager:new Ext.WindowGroup(),
    title : '消息提醒',
    html : '消息提醒',

    constructor: function(){
        this.callParent(arguments);
        this.initPosition();
        this.clearListeners();
    },
    //参数flag为true时强制更新位置  
    initPosition: function() {
        var doc = document, bd = (doc.body || doc.documentElement);  
        //Ext取可视范围宽高(与上面方法取的值相同), 加上滚动坐标  
        var left = bd.scrollLeft + window.innerWidth-4-this.width;  
        var top = bd.scrollTop + window.innerHeight-mainApplication.desktop.taskbar.getHeight()-this.height;//底部包含开始菜单
        this.setPosition(left, top);  
    },
    
    afterRender:function(){
    	var me = this;
    	//自动隐藏
    	if (false !== me.autoHide){
            var task = new Ext.util.DelayedTask(me.hide, me),
            second = (parseInt(me.autoHide) || 3) * 1000;
            task.delay(second);
        }
    	me.on('afterrender', me.showTips);
    	me.on('beforehide', me.hideTips);
        Ext.EventManager.onWindowResize(me.initPosition, me); //window大小改变时，重新设置坐标
        Ext.EventManager.on(window, 'scroll', me.initPosition, me); //window移动滚动条时，重新设置坐标
    	me.callParent(arguments);
    },
    showTips: function() {  
        var self = this;
        //初始化坐标
        self.initPosition();
        self.el.slideIn('b', {  
            callback: function() {   
            	//显示完成后,手动触发show事件   
                self.fireEvent('show', self);  
            },
            duration: 1000
        });
        self.PlayAlarm();
        //不执行默认的show
        return false;   
    },  
    hideTips: function() {  
        var self = this;
        if(self.hidden){return false;}  
        self.el.slideOut('b', {
            callback: function() {  
                self.suspendEvent('beforehide');
                self.close();
            },
            duration: 1000
        });
        //不执行默认的close
        return false;
    },
    PlayAlarm:function(){
		var audio = new Audio('resources/resources/msg/alarm.mp3');
        audio.play();
        audio.addEventListener('ended',function(e){
        	audio.pause();
			delete audio;
        });
    }
});
