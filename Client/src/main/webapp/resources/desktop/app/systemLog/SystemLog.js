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
 
Ext.define('Wdesktop.app.systemLog.SystemLog', {
	extend : 'Ext.panel.Panel',

	requires : [ 'Ext.form.field.TextArea', 'Ext.toolbar.Paging' ],

	height : 354,
	width : 568,
	layout : 'border',
	frameHeader : false,
	header : false,

	initComponent : function() {
		var me = this;
		me.store = me.getSystemLogStore();
		Ext.applyIf(me, {
			items : [ {
				xtype : 'textareafield',
				region : 'center',
				style: {
					'font-size': '15px'
		        },
				readOnly : true
			} ],
			dockedItems : [
					{
						xtype : 'pagingtoolbar',
						dock : 'bottom',
						store : me.store,
						displayInfo : true
					},
					{
						xtype : 'toolbar',
						dock : 'top',
						layout : 'fit',
						defaults : {
							labelWidth : 40
						},
						items : [ Ext.create(
								'Wdesktop.app.systemLog.SystemLogSearch', {
									parent : me
								}) ]
					} ]
		});
		me.textareafield = null;
		me.store.on('load', function(s, records, successful, eOpts) {
			if (!me.textareafield) {
				me.textareafield = me.down('textareafield');
			}
			me.textareafield.setValue('');
			if (successful) {
				Ext.Array.each(records, function(model) {
					me.addLine(model['data']);
				});
			}
		}, me);
		me.callParent(arguments);
	},
	getSystemLogStore : function() {
		return Ext.create('Wdesktop.app.systemLog.SystemLogStore');
	},
	addLine : function(data) {
		var me = this, textareafield = me.textareafield;
		var values = textareafield.getValue();
		var newValue = '[' + data['threadName'] + ']-[' + data['lever'] + '] '
				+ data['logDateTime'] + ' ' + data['classPath'] + ' '
				+ data['message'] + '\n';
		textareafield.setValue(values + newValue);
	}

});
