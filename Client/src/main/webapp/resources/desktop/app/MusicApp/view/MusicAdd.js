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
 
Ext.define('Wdesktop.app.MusicApp.view.MusicAdd', {
    extend: 'Ext.form.Panel',
    alias : "widget.musicadd",
    frame: false,
    height: 469,
    width: 660,
    layout: {
        type: 'absolute'
    },
    bodyPadding: 10,
    method : 'post',
	url : 'media/music/add.json',
	timeout : 100000,
	waitTitle : '正在上传中...',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            defaults: {
                labelWidth: 40
            },
            items: [
                {
                    xtype: 'fieldset',
                    x: 10,
                    y: 10,
                    frame: false,
                    height: 190,
                    width: 280,
                    defaults: {
                        labelWidth: 60
                    },
                    layout: {
                        type: 'absolute'
                    },
                    title: '歌曲信息',
                    items: [
                        {
                            xtype: 'textfield',
                            x: 10,
                            y: 0,
                            width: 240,
                            name: 'title',
                            readOnly: false,
                            allowBlank: false,
                            fieldLabel: '歌曲名称',
                            emptyText: '请输入歌曲名称',
                            maxLength: 80,
                            minLength: 1
                        },
                        {
                            xtype: 'textfield',
                            x: 10,
                            y: 30,
                            width: 240,
                            name: 'singer',
                            fieldLabel: '歌手',
                            allowBlank: false,
                            emptyText: '请输入歌手，可以多个',
                            maxLength: 50,
                            minLength: 2
                        },
                        {
                            xtype: 'combobox',
                            x: 10,
                            y: 60,
                            width: 240,
                            name: 'type',
                            allowBlank : false,
        					emptyText : '请选择类型',
        					queryMode : "local",
        					multiSelect : true,
        					fieldLabel: '类型',
                            store : me.getMusicTypeStore(),
                            valueField : "name",
        					displayField : "name",
        					name : 'type',
        					forceSelection : true,
        					editable : false
                        },{
                        	xtype:'label',
                        	x: 10,
                            y: 90,
                            text:'时间长度:'
                        },{
                            xtype: 'numberfield',
                            x: 75,
                            y: 90,
                            allowBlank: false,
                            readOnly: true,
                            name: 'duration'
                        },{
                        	xtype:'label',
                        	x: 240,
                            y: 90,
                            text:'秒'
                        },{
                            xtype: 'numberfield',
                            x: 10,
                            y: 120,
                            width: 240,
                            name: 'year',
                            fieldLabel: '年份',
                            allowBlank: false,
                            emptyText: '请输入歌曲发行年份',
                            decimalPrecision: 0
                        }
                    ]
                },
                {
                    xtype: 'fieldset',
                    x: 310,
                    y: 10,
                    height: 190,
                    width: 320,
                    defaults: {
                        labelWidth: 60
                    },
                    layout: {
                        type: 'absolute'
                    },
                    title: '基本信息',
                    items: [
                        {
                            xtype: 'textfield',
                            x: 30,
                            y: 0,
                            width: 260,
                            fieldLabel: '文件大小',
                            emptyText: '文件上传后,自动显示,单位:K',
                            readOnly: true
                        },
                        {
                            xtype: 'textfield',
                            x: 30,
                            y: 40,
                            disabled: false,
                            width: 260,
                            readOnly: true,
                            fieldLabel: '上传用户',
                            value:comm.get('user_id')
                        },
                        {
                            xtype: 'textfield',
                            x: 30,
                            y: 80,
                            width: 260,
                            readOnly: true,
                            fieldLabel: '用户姓名',
                            value:comm.get('user_name')
                        },
                        {
                            xtype: 'displayfield',
                            x: 30,
                            y: 120,
                            width: 260,
                            value: returnCitySN.cip,
                            fieldLabel: '本机IP'
                        }
                    ]
                },
                {
                    xtype: 'fieldset',
                    x: 10,
                    y: 200,
                    height: 355,
                    layout: {
                        type: 'absolute'
                    },
                    collapsed: false,
                    collapsible: false,
                    title: '歌曲高级信息',
                    items: [{
                		xtype: 'filefield',
                        x: 0,
                        y: 0,
                        name: 'musicFile',
                        fieldLabel: '歌曲上传(MP3格式)',
                        labelAlign: 'top',
                        allowBlank: true,
                        emptyText: '文件选择后会对路径加密',
                        enableKeyEvents: true,
                        buttonMargin: 6,
                        buttonOnly: false,
                        buttonText: '浏览',
                        validator:function(value){
                        	var ext = [ '.mp3','.ogg' ];
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
                    },
                        {
                            xtype: 'htmleditor',
                            x: 0,
                            y: 50,
                            frame: false,
                            height: 170,
                            style: 'background-color: white;',
                            enableAlignments: false,
                            enableFontSize: false,
                            enableLists: false,
                            name: 'description',
                            fieldLabel: '歌曲描述',
                            labelAlign: 'top'
                        }]
                },{
                    xtype: 'button',
                    x: 100,
                    y: 460,
                    height: 30,
                    width: 70,
                    text: '提交',
                    handler:me.onMusicFormSubmit
                },{
                    xtype: 'button',
                    x: 400,
                    y: 460,
                    height: 30,
                    width: 80,
                    text: '重置',
    				handler:function(btn){
    					btn.up('musicadd').getForm().reset();
    				}
                }]
        });

        me.callParent(arguments);
    },
    getMusicTypeStore:function(){
    	return Ext.create('Ext.data.Store', {
			fields : [ 'name' ],
			data : [{
				name : "古典音乐"
			}, {
				name : "宗教音乐"
			}, {
				name : "流行音乐"
			}, {
				name : "重金属音乐"
			}, {
				name : "摇滚乐"
			}, {
				name : "电子音乐"
			}, {
				name : "爵士乐"
			},{
				name : "其他"
			}]
    	});
    },
	onFilefieldChange : function(filefield, value, eOpts) {
		if (!filefield.isValid()) {
			return;
		}
		filefield.setDisabled(true);
		var fileSize = (filefield.fileInputEl.dom.files[0].size / 1024)
				.toFixed(2);
		var fieldsets = filefield.up('fieldset').up('musicadd')
		.query('fieldset');
		
		var fieldset = fieldsets[1];
		fieldset.down('textfield').setValue(fileSize + "K");
		
		var url = URL.createObjectURL(filefield.fileInputEl.dom.files[0]);
		var audio = new Audio(url);
		audio.volume=0;
		audio.play();
		audio.addEventListener('loadeddata',function(){
			fieldsets[0].query('numberfield')[0].setValue(Math.round(audio.duration));
			audio.pause();
			delete audio;
			filefield.setDisabled(false);
		});
	},
	onMusicFormSubmit:function(btn){
		
		var music_form = btn.up('musicadd').getForm();

		if (music_form.isValid()) {
			music_form.submit({
				waitMsg : '表单提交中',
				success : function(form, action) {
					form.reset();
					Ext.Msg.show({
						title : '系统提示',
						msg : '音乐上传成功,请等待管理员审核',
						buttons : Ext.Msg.OK,
						closable : false
					});
				},
				failure : function(form, action) {
					Ext.Msg.show({
						title : '错误',
						msg : data['error'],
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
