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
 
Ext.define('Wdesktop.app.MovieApp.view.MovieAdd', {
	extend : 'Ext.form.Panel',
	alias : "widget.movieadd",
	frame : false,
	height : 410,
	width : 660,
	layout : {
		type : 'absolute'
	},
	bodyPadding : 10,
	method : 'post',
	url : 'media/movie/add.json',
	timeout : 100000,
	waitTitle : '正在上传中...',
	maxFileSize:4194304,//4G

	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			items : [ {
				xtype : 'fieldset',
				x : 0,
				y : 0,
				height : 230,
				width : 330,
				defaults : {
					labelWidth : 60
				},
				layout : {
					type : 'absolute'
				},
				title : '影片基本信息',
				items : [ {
					xtype : 'textfield',
					x : 10,
					y : 0,
					width : 280,
					allowBlank : false,
					name : 'title',
					fieldLabel : '影片名称',
					emptyText : '请输入影片名称',
					maxLength : 99,
					minLength : 1
				}, {
					xtype : 'textfield',
					x : 10,
					y : 35,
					width : 280,
					allowBlank : false,
					name : 'actor',
					fieldLabel : '主演',
					emptyText : '请输入主演,可写多个',
					maxLength : 99,
					minLength : 2
				}, {
					xtype : 'textfield',
					x : 10,
					y : 70,
					width : 280,
					allowBlank : false,
					name : 'author',
					fieldLabel : '导演',
					emptyText : '请输入导演,可写多个',
					maxLength : 49,
					minLength : 2
				}, {
					xtype : 'datefield',
					x : 10,
					y : 105,
					width : 280,
					allowBlank : false,
					name : 'madeDate',
					fieldLabel : '出版时间',
					emptyText : '请输入出版时间',
					altFormats : 'Y,m,d|Y.m.d|Y/m/d|Y-m-d',
					format : 'Y年m月d日',
					submitFormat : 'Y-m-d',
					editable : false,
					maxValue : new Date()
				}, {
					xtype : 'combobox',
					x : 10,
					y : 140,
					width : 280,
					fieldLabel : '影片类型',
					allowBlank : false,
					emptyText : '请选择类型',
					queryMode : "local",
					multiSelect : true,
					queryMode : 'local',
					store : me.getMovieTypeStore(),
					valueField : "name",
					displayField : "name",
					name : 'type',
					forceSelection : true,
					editable : false
				}, {
					xtype : 'label',
					x : 10,
					y : 170,
					text : '时间长度:'
				}, {
					xtype : 'numberfield',
					x : 75,
					y : 170,
					allowBlank : false,
					name : 'duration',
					minValue : 1
				}, {
					xtype : 'label',
					x : 280,
					y : 170,
					text : '秒'
				} ]
			}, {
				xtype : 'fieldset',
				x : 0,
				y : 240,
				height : 150,
				width : 330,
				defaults : {
					labelWidth : 60
				},
				layout : {
					type : 'absolute'
				},
				title : '用户基本信息',
				items : [ {
					xtype : 'textfield',
					x : 10,
					y : 0,
					width : 280,
					emptyText : '选择文件后,自动显示,单位:K',
					readOnly : true,
					fieldLabel : '文件大小'
				}, {
					xtype : 'textfield',
					x : 10,
					y : 30,
					width : 280,
					readOnly : true,
					fieldLabel : '上传用户',
					value : comm.get('user_id')
				}, {
					xtype : 'textfield',
					x : 10,
					y : 60,
					readOnly : true,
					width : 280,
					fieldLabel : '用户姓名',
					value : comm.get('user_name')
				}, {
					xtype : 'displayfield',
					x : 10,
					y : 90,
					width : 280,
					value : returnCitySN.cip,
					fieldLabel : '本机IP'
				} ]
			}, {
				xtype : 'fieldset',
				x : 340,
				y : 0,
				height : 355,
				defaults : {
					labelWidth : 40
				},
				layout : {
					type : 'absolute'
				},
				title : '影片高级信息',
				items : [ {
					xtype : 'filefield',
					x : 0,
					y : 0,
					// width: 250,
					fieldLabel : '影片文件(MP4)',
					emptyText : '文件选择后会对路径加密',
					enableKeyEvents : true,
					buttonMargin : 6,
					name : 'movieFile',
					labelAlign : 'top',
					buttonText : '浏览',
					validator : function(value) {
						var ext = [ '.mp4' ];
						var s = value.toLowerCase();
						for (var i = 0; i < ext.length; i++) {
							if (s.indexOf(ext[i]) > 0) {
								return true;
							}
						}
						return '文件类型错误';
					},
					listeners : {
						change : {
							fn : me.onFilefieldChange,
							scope : me
						}
					}
				}, {
					xtype : 'htmleditor',
					anchor : '',
					x : 0,
					y : 50,
					height : 270,
					style : 'background-color: white;',
					enableAlignments : false,
					enableColors : false,
					enableFontSize : false,
					enableLinks : false,
					allowBlank : false,
					name : 'description',
					enableLists : false,
					enableSourceEdit : false,
					fieldLabel : '描述',
					hideLabel : false,
					labelAlign : 'top'
				} ]
			}, {
				xtype : 'button',
				x : 360,
				y : 365,
				height : 30,
				width : 60,
				formBind : true, // only enabled once the form is valid
				disabled : true,
				text : '提交',
				handler : me.onFormButtonClick
			}, {
				xtype : 'button',
				x : 560,
				y : 365,
				height : 30,
				width : 60,
				text : '重置'
			} ]
		});

		me.callParent(arguments);
	},
	getMovieTypeStore : function() {
		return Ext.create('Ext.data.Store', {
			fields : [ 'name' ],
			data : [ {
				name : "动作"
			}, {
				name : "冒险"
			}, {
				name : "喜剧"
			}, {
				name : "爱情"
			}, {
				name : "战争"
			}, {
				name : "恐怖"
			}, {
				name : "武侠"
			}, {
				name : "悬疑"
			}, {
				name : "科幻"
			}, {
				name : "动画"
			}, {
				name : "奇幻"
			}, {
				name : "青春"
			}, {
				name : "励志"
			}, {
				name : "历史"
			}, {
				name : "剧情"
			}, {
				name : "其他"
			} ]
		});
	},
	onFilefieldChange : function(filefield, value, eOpts) {
		if (!filefield.isValid()) {
			return;
		}
		filefield.setDisabled(true);
		var fileSize = (filefield.fileInputEl.dom.files[0].size / 1024)
				.toFixed(2);
		
		if(filefield.up('fieldset').up('movieadd').maxFileSize){
			if(fileSize>=filefield.up('fieldset').up('movieadd').maxFileSize){
				filefield.reset();
				Ext.newstip.msg('系统提示', '视频文件最大不超过4G',3000);
				return;
			}
		}
				
		var fieldsets = filefield.up('fieldset').up('movieadd')
		.query('fieldset');
		
		var fieldset = fieldsets[1];
		fieldset.down('textfield').setValue(fileSize + "K");
		
		var video = document.createElement("video");
		var url = URL.createObjectURL(filefield.fileInputEl.dom.files[0]);
		video.src=url;
		video.volume=0;
		video.play();
		video.addEventListener('loadeddata',function(){
			fieldsets[0].query('numberfield')[0].setValue(Math.round(video.duration));
			video.pause();
			delete video;
			filefield.setDisabled(false);
		});
		
	},
	onFormButtonClick : function(btn) {
		var music_form = btn.up('movieadd').getForm();

		if (music_form.isValid()) {
			music_form.submit({
				waitMsg : '表单提交中',
				success : function(form, action) {
					form.reset();
					Ext.Msg.show({
						title : '系统提示',
						msg : '视频上传成功,请等待管理员审核',
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

		} else {
			Ext.MessageBox.alert('错误',
					'<font color=red size=3 face="华文楷体">请按要求填写.</font>', null,
					this);
		}
	}

});
