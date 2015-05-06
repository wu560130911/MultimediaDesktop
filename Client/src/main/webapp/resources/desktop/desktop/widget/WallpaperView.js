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
 
Ext
		.define(
				'Wdesktop.desktop.widget.WallpaperView',
				{
					extend : 'Ext.view.View',
					alias : 'widget.wallpaperView',
					mixins : {
						dragSelector : 'Ext.ux.DataView.DragSelector',
						draggable : 'Ext.ux.DataView.Draggable'
					},

					tpl : [
							'<tpl for=".">',
							'<div class="thumb-wrap">',
							'<div class="thumb">',
							(!Ext.isIE6 ? '<img src="{path}" />'
									: '<div style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src=\'{path}\')"></div>'),
							'</div>',
							// '<span>{name}</span>',
							'</div>', '</tpl>' ],

					itemSelector : 'div.thumb-wrap',
					multiSelect : false,
					singleSelect : true,
					cls : 'x-image-view',
					autoScroll : true,

					initComponent : function() {
						var me = this;
						me.mixins.dragSelector.init(me);
						me.mixins.draggable.init(me, {
							ddConfig : {
								ddGroup : 'organizerDD',
								enableDD : true
							}
						});
						me.callParent(arguments);
					},

					listeners : {
						itemcontextmenu : function(me, record, item, index, e) {
							e.stopEvent();
						},
						itemdblclick : function(me, record, item, index, e,
								eOpts) {
						},
						itemclick : function(me, record, item, index, e, eOpts) {
						}
					}
				});
