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
 

Ext.define('Wdesktop.desktop.widget.AudioWindow', {
    extend: 'Wdesktop.core.system.Module',

    uses: [
        'Wdesktop.core.widget.Audio'
    ],
    id:'audio',
    play_title:'请选择歌曲',
    play_singer:'请选择歌曲',

    init : function(){
    	/*var me = this;
    	Ext.onReady(function(){
    		return me.createWindow();
    	});*/
    },

    createWindow : function(){
        var me = this, desktop = me.app.getDesktop(),
            win = desktop.getWindow(me.id);
        if (!win) {
            win = desktop.createWindow({
                id: me.id,
                title: '音乐播放器',
                width: 315,
                height: 137,
                iconCls: 'music_main_mini',
                animCollapse: false,
                border: false,
                maximizable:false,
                constrainHeader: true,
                layout: 'absolute',
                defaults:{
                	labelWidth:80
                },
                listeners:{
                	beforeclose:function(){
                		
                	}
                },
                items: [{
                	xtype:'displayfield',
                	x:10,
                	y:10,
                	fieldLabel:'<font size=3 face="华文楷体">歌曲名称</font>',
                	value:'<font size=3 face="华文楷体" color=red>'+me.play_title+'</font>'
                },{
                	xtype:'displayfield',
                	x:10,
                	y:30,
                	fieldLabel:'<font size=3 face="华文楷体">歌手</font>',
                	value:'<font size=3 face="华文楷体" color=red>'+me.play_singer+'</font>'
                },{
                        xtype: 'audio',
                        x:0,
                        y:57,
                        src: '',
                        autobuffer: true,
                        controls : true/* default 如果出现该属性，则向用户显示控件，比如播放按钮。*/
                    }
                ]
            });
        }
        win.show();
        return win;
    }

});
