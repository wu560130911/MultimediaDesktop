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
 
Ext.define('Wdesktop.core.theme.ThemeView', {
	extend : 'Ext.view.View',
	
	itemSelector : 'div.ux-theme-item-wrap',
	
	 tpl: [
            '<tpl for=".">',
                '<div class="ux-theme-item-wrap" id="{name}">',
                '<div class="ux-theme-item"><img src="{imgPath}" title="{name}"></div>',
                '<span>{name}</span></div>',
            '</tpl>',
            '<div class="x-clear"></div>'
        ],
     multiSelect: false,
     
     overItemCls: 'ux-theme-item-over',
   
     selectedItemCls : 'ux-theme-item-selected',
     
     trackOver : true,
     
     initComponent : function(){
     	var me = this;
     	me.callParent(arguments);
     }
});
