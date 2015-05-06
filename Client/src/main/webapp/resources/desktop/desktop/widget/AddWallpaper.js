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
 
Ext.define('Wdesktop.desktop.widget.AddWallpaper', {
	extend : 'Ext.form.Panel',
	alias : 'widget.addWallpaper',
	requires : [ 'Ext.form.field.File', 'Ext.form.field.ComboBox', 'Ext.Img' ],
	bodyPadding : 10,
	title : '添加壁纸',
	autoScroll : true,
	method : 'post',
	url : 'wallpaper/add.json',

	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			defaults : {
				labelWidth : 60
			},
			items : [ {
				xtype : 'filefield',
				anchor : '100%',
				fieldLabel : '图片文件',
				buttonText : '浏览',
				name : 'imageFile',
				allowBlank : false,
				editable : false,
				validator : function(value) {
					var ext = [ '.gif', '.jpg', '.jpeg', '.png' ];
					var s = value.toLowerCase();
					for (var i = 0; i < ext.length; i++) {
						if (s.indexOf(ext[i]) > 0) {
							return true;
						}
					}
					return "请选择图片文件";
				},
				listeners : {
					change : {
						fn : me.onFilefieldChange,
						scope : me
					}
				}
			}, {
				xtype : 'combobox',
				anchor : '100%',
				fieldLabel : '图片类型',
				store : me.comboboxStore,
				queryMode : 'local',
				displayField : 'name',
				name : 'type',
				valueField : 'name',
				allowBlank : false,
				editable : false,
				repeatTriggerClick : true
			}, {
				xtype : 'textfield',
				anchor : '100%',
				fieldLabel : '添加用户',
				readOnly : true,
				value : comm.get('user_id')
			}, {
				xtype : 'button',
				width : 90,
				text : '提交',
				formBind : true, // only enabled once the form is valid
				disabled : true,
				handler : me.onButtonClick
			}, {
				xtype : 'image',
				anchor : '100%'
			// height:280
			} ]
		});

		me.callParent(arguments);
	},

	onButtonClick : function() {

		var me = this;
		var formPanel = me.up('addWallpaper');
		var form = formPanel.getForm();
		if (form.isValid()) {
			form.submit({
				success : function(form, action) {
					form.reset();
					var img = Ext.ComponentQuery.query('addWallpaper')[0]
							.down('image');
					img.setSrc(Ext.BLANK_IMAGE_URL);
					Ext.Msg.show({
						title : '系统提示',
						msg : '壁纸上传成功',
						buttons : Ext.Msg.OK,
						closable : false
					});
				},
				failure : function(form, action) {
					Ext.Msg.show({
						title : '错误',
						msg : action.result.error,
						buttons : Ext.Msg.OK,
						closable : false
					});
				}
			});
		}
	},

	onFilefieldChange : function(filefield, value, eOpts) {
		if (!filefield.isValid()) {
			return;
		}
		var img = Ext.ComponentQuery.query('addWallpaper')[0].down('image');
		var reader = new FileReader();
		reader.onload = (function(aImg) {
			return function(e) {
				aImg.setSrc(e.target.result);
			};
		})(img);
		reader.readAsDataURL(filefield.fileInputEl.dom.files[0]);
	}

});
