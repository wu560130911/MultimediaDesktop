/**
 *
 */
Ext.define('Ext.ux.DataView.LabelEditor', {

    extend: 'Ext.Editor',

    alignment: 'tl-tl',

    completeOnEnter: true,

    cancelOnEsc: true,

    shim: false,

    autoSize: {
        width: 'boundEl',
        height: 'field'
    },

    labelSelector: 'x-editable',

    requires: [
        'Ext.form.field.Text'
    ],

    constructor: function(config) {
        config.field = config.field || Ext.create('Ext.form.field.Text', {
            allowOnlyWhitespace: false,
            selectOnFocus:true
        });
        this.callParent([config]);
    },

    init: function(view) {
        this.view = view;
        this.mon(view, 'render', this.bindEvents, this);
        this.on('complete', this.onSave, this);
    },

    // initialize events
    bindEvents: function() {
        this.mon(this.view.getEl(), {
        	dblclick: {
                fn: this.onDblClick,
                scope: this
            },
            click:{
            	fn: this.onClick,
                scope: this
            }
        });
    },

    // on mousedown show editor
    onDblClick: function(e, target) {
        var me = this,
             record;
        target = me.view.findItemByChild(target);
        if (target&&Ext.fly(target).hasCls(me.labelSelector) && !me.editing && !e.ctrlKey && !e.shiftKey) {
            e.stopEvent();
            //item = me.view.findItemByChild(target);
            record = me.view.store.getAt(me.view.indexOf(target));
            me.startEdit(Ext.get(target).dom.childNodes[1], record.raw[me.dataIndex]);
            me.activeRecord = record;
        } else if (me.editing) {
            me.field.blur();
            e.preventDefault();
        }
    },
    onClick:function(e, target){
    	var me = this;
    	target = me.view.findItemByChild(target);
    	if(!target&&me.editing){
    		me.field.blur();
            e.preventDefault();
    	}
    },
    // update record
    onSave: function(ed, value) {
    	var me = this;
        this.activeRecord.raw[me.dataIndex]=value;
        if(value.length>4){
        	value = value.substr(0,4)+'...';
        }
        this.activeRecord.set(me.dataIndex, value);
    }
});

