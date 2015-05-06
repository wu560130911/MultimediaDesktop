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
 

Ext.define('Wdesktop.desktop.widget.VideoWindow', {
    extend: 'Wdesktop.core.system.Module',

    uses: [
        'Wdesktop.core.widget.Video'
    ],
    
    requires:['Wdesktop.desktop.widget.PlayList','Wdesktop.core.widget.Video'],
    
    id:'video',

    tipWidth: 160,
    tipHeight: 110,
    
    init : function(){
    },

    /* createWindow uses renderTo, so it is immediately rendered */
    createWindow : function(){
        var me = this, desktop = me.app.getDesktop(),
            win = desktop.getWindow(me.id);
        if (!win) {
            win = desktop.createWindow({
                id: me.id,
                title: '视频播放器',
                width: 800,
                height: 520,
                iconCls: 'video_play_main_mini',
                animCollapse: false,
                closeAction:'destroy',
                border: false,
                constrainHeader: true,
                layout : {
            		type : 'border'
            	},
                items: [{
                	xtype : 'playlist',
                	region : 'east'
                },{
                        xtype: 'video',
                        region : 'center',
                        poster:'resources/resources/wallpapers/blue.jpg',
                        src: [],
                        autobuffer: true,
                        controls : true,/* default 如果出现该属性，则向用户显示控件，比如播放按钮。*/
                        listeners: {
                            afterrender: function(video) {
                            	me.videoEl = document.getElementById("video_play");
                            	
                            	Ext.onReady(function(){
                            		me.tip = Ext.create('Ext.tip.ToolTip', {
                                		anchor   : 'bottom',
                                        dismissDelay : 0,
                                        target:  win.animateTarget,
                                        height   : me.tipHeight,
                                        width    : me.tipWidth,
                                        trackMouse : true,
                                        renderTo: Ext.getBody(),
                                        tpl: [
                                            '<canvas width="{width}" height="{height}">'
                                        ],
                                        data: {
                                        	height   : me.tipHeight,
                                            width    : me.tipWidth
                                        },
                                        renderSelectors: {
                                            body: 'canvas'
                                        },
                                        listeners: {
                                            afterrender: me.onTooltipRender,
                                            show: me.renderPreview,
                                            scope: me
                                        }
                                	});
                            	});
                            }
                        }
                }],
                listeners: {
                    beforedestroy: function() {
                        me.tip = me.ctx = me.videoEl = null;
                    },
                    afterrender:function(){
                    	
                    }
                }
            });
        }
        win.show();
        return win;
    },

    onTooltipRender: function (tip) {
    	var el = tip.body.dom, me = this;
        me.ctx = el.getContext && el.getContext('2d');
    },
    renderPreview: function() {
        var me = this;
        if ((me.tip && !me.tip.isVisible()) || !me.videoEl) {
            return;
        }
        if (me.ctx) {
            try {
                me.ctx.drawImage(me.videoEl, 0, 0, me.tipWidth, me.tipHeight);
            } catch(e) {};
        }
        Ext.Function.defer(me.renderPreview, 20, me);
    }
});
