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
 
Ext.define('Wdesktop.module.WallpaperModule', {
    extend: 'Wdesktop.core.system.Module',

    uses: [
        'Wdesktop.desktop.widget.WallpaperManager'
    ],
    id:'wallpaperManager',
    windowId: 'wallpaperManager-window',

    init : function(){
        
    },

    createWindow : function(){
        var me = this, desktop = me.app.getDesktop(),
            win = desktop.getWindow(me.id);
        if (!win) {
            win = desktop.createWindow({
                id: me.id,
                title: '壁纸管理',
                width: 880,
                height: 480,
                iconCls: 'wallpaper_cls_mini',
                border: false,
                hideMode: 'offsets',
                closable:true,
                closeAction:"destroy",
                layout:"fit",
                items:{xtype: 'wallpaperManager'}
            });
        }
        win.show();
        return win;
    }

});
