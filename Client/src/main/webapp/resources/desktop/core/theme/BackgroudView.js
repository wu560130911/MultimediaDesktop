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
 
Ext.define('Wdesktop.core.theme.theme.BackgroudView', {
	extend : 'Ext.view.View',
	
	itemSelector : 'div.ux-backgroud-item-wrap',
	
	 tpl: [
            '<tpl for=".">',
                '<div class="ux-backgroud-item-wrap" id="{name}">',
                '<div class="ux-backgroud-item"><img src="{url}" title="{name}"></div>',
                '<span>{text}</span></div>',
            '</tpl>',
            '<div class="x-clear"></div>'
        ],
     multiSelect: false,
     
     overItemCls: 'ux-backgroud-item-over',
   
     selectedItemCls : 'ux-backgroud-item-selected',
     
     trackOver : true,
     
     initComponent : function(){
     	var me = this;
     	me.callParent();
     }
});
