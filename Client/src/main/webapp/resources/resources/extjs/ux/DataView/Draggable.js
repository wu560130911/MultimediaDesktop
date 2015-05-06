Ext.define('Ext.ux.DataView.Draggable', {
    requires: 'Ext.dd.DragZone',

    ghostCls: 'x-dataview-draggable-ghost',

    ghostTpl: [
        '<tpl for=".">',
            '{title}',
        '</tpl>'
    ],

    init: function(dataview, config) {
    	var me = this;
    	
        me.dataview = dataview;

        dataview.on({
        	scope: me,
        	boxready:{
        		fn:me.onRender
        	},
        	beforedestroy: {
        	   fn:me.onContainerDestroy
           }
        });
        Ext.apply(this, {
            itemSelector: dataview.itemSelector,
            ghostConfig : {}
        }, config || {});

        Ext.applyIf(this.ghostConfig, {
            itemSelector: 'img',
            cls: this.ghostCls,
            tpl: this.ghostTpl
        });
    },
    
    /**
     * @private Clear up on dataview destroy
     */
    onContainerDestroy: function() {
        var dragZone = this.dragZone;
        var drogZone = this.dropZone;
        if (dragZone) {
        	dragZone.unreg();
            this.dragZone = null;
        }
        if(drogZone){
        	drogZone.unreg();
        	this.drogZone=null;
        }
    },
    
    /**
     * @private
     * Called when the attached DataView is rendered. Sets up the internal DragZone
     */
    onRender: function() {
    	var me = this;
    	
        var dragconfig = Ext.apply({}, me.ddConfig || {}, {
            dvDraggable: me,
            dataview   : me.dataview,
            getDragData: me.getDragData,
            afterRepair: me.afterRepair,
            getRepairXY: me.getRepairXY,
            //onMouseUp:me.onMouseUp,
            //onMouseDown:me.onMouseDown
        });
        
        var dropconfig = Ext.apply({}, me.ddConfig || {}, {
            dvDraggable: me,
            dataview   : me.dataview,
            isMyself: me.isMyself,
            getTargetFromEvent: me.getTargetFromEvent,
            move: me.move,
            //onNodeOver:me.onNodeOver,
            //onNodeEnter:me.onNodeEnter,
            notifyDrop:me.notifyDrop
        });

        /**
         * @property dragZone
         * @type Ext.dd.DragZone
         * The attached DragZone instane
         */
        me.dragZone = Ext.create('Ext.dd.DragZone', me.dataview.getEl(), dragconfig);
        me.dropZone = Ext.create('Ext.dd.DropZone',me.dataview.getEl(),dropconfig);
    	 
    },

    getDragData: function(e) {
    	
        var draggable = this.dvDraggable,
            dataview  = this.dataview,
            selModel  = dataview.getSelectionModel(),
            target    = e.getTarget(draggable.itemSelector),
            selected, dragData;

        if (target) {
            if (!dataview.isSelected(target)) {
                selModel.select(dataview.getRecord(target));
            }

            selected = dataview.getSelectedNodes();
            dragData = {
                copy: true,
                nodes: selected,
                records: selModel.getSelection(),
                item: true
            };

            if (selected.length == 1) {
                dragData.single = true;
                dragData.ddel = target;
            } else {
                dragData.multi = true;
                dragData.ddel = draggable.prepareGhost(selModel.getSelection()).dom;
            }
            return dragData;
        }

        return false;
    },

    afterRepair: function() {
        this.dragging = false;

        var nodes  = this.dragData.nodes,
            length = nodes.length,
            i;

        //FIXME: Ext.fly does not work here for some reason, only frames the last node
        for (i = 0; i < length; i++) {
            Ext.get(nodes[i]).frame('#8db2e3', 1);
        }
    },

    /**
     * @private
     * Returns the x and y co-ordinates that the dragged item should be animated back to if it was dropped on an
     * invalid drop target. If we're dragging more than one item we don't animate back and just allow afterRepair
     * to frame each dropped item.
     */
    getRepairXY: function(e) {
        if (this.dragData.multi) {
            return false;
        } else {
            var repairEl = Ext.get(this.dragData.ddel),
                repairXY = repairEl.getXY();

            //take the item's margins and padding into account to make the repair animation line up perfectly
            repairXY[0] += repairEl.getPadding('t') + repairEl.getMargin('t');
            repairXY[1] += repairEl.getPadding('l') + repairEl.getMargin('l');

            return repairXY;
        }
    },

    /**
     * Updates the internal ghost DataView by ensuring it is rendered and contains the correct records
     * @param {Array} records The set of records that is currently selected in the parent DataView
     * @return {Ext.view.View} The Ghost DataView
     */
    prepareGhost: function(records) {
        var ghost = this.createGhost(records),
            store = ghost.store;

        store.removeAll();
        store.add(records);

        return ghost.getEl();
    },

    createGhost: function(records) {
        if (!this.ghost) {
            var ghostConfig = Ext.apply({}, this.ghostConfig, {
                store: Ext.create('Ext.data.Store', {
                    model: records[0].modelName
                })
            });

            this.ghost = Ext.create('Ext.view.View', ghostConfig);

            this.ghost.render(document.createElement('div'));
        }

        return this.ghost;
    },
    
    moveShortcut : function(target,source){
    	var me = this;
    	var sourceIndex = me.dataview.indexOf(source);
    	var sourceRecord  = me.dataview.store.getAt(sourceIndex);
    	if(target){
    		var targetIndex = me.dataview.indexOf(target);
    		me.dataview.store.remove(sourceRecord);
    		me.dataview.store.insert(targetIndex,sourceRecord);
    	}else{
    		me.dataview.store.remove(sourceRecord);
    		me.dataview.store.add(sourceRecord);
    	}
    },
    
    /*onMouseDown:function(e){
    	var me = this;
    	me.bdata = me.getDragData(e);
    },
    onMouseUp:function(e){
    	var me = this;
    	var adata = me.getDragData(e);
    	Ext.get(adata.ddel).insertAfter(Ext.get(me.bdata.ddel).el);
    	console.log(Ext.get(adata.ddel));
    	if(me.bdata.single){
    		
    	}
    }*/
    //自己和自己不应该反应  
    //@param target{HTMLElement} 当前的hover对象  
    //@param data.ddel{HTMLElement} 当前的drag对象  
    isMyself: function (target, data) {  
        return target == data.ddel;  
    },
    //当前鼠标滑是否是可drop子区域  
    getTargetFromEvent: function (e) {
        return e.getTarget(this.dvDraggable.itemSelector);  
    },
    //根据鼠标在drop目标的位置决定占位符是否应该插前还是插后  
    move: function (target, data, e) {  
        target = Ext.get(target);  
        var tx = target.getX();  
        var ex = e.getPageX();  
        var tw = target.getWidth();  
        var del = Ext.get(data.ddel);  
        if (ex - tx < (tw / 2)) {  
            //是否需要改变位置?  
            if (target.prev("div") && target.prev("div").dom == data.ddel) return;  
            del.insertBefore(target);  
        } else {  
            //是否需要改变位置?  
            if (del.prev("div") && del.prev("div").dom == target.dom) return;  
            target.insertBefore(del);  
        }  
    },
    /* 
    	在进入节点以及在节点间移动时，判断鼠标位置，开始移动占位符 
     */  
    /*onNodeOver: function (target, dd, e, data) {  
    	if (this.isMyself(target, data)) return;  
    	this.move(target, data, e);
    	console.log('over'); 
    }, 
    onNodeEnter: function (target, dd, e, data) {  
    	if (this.isMyself(target, data)) return;  
    	this.move(target, data, e);
    	console.log(target);
    },*/ 
    notifyDrop:function (source, e, data) {
    	var me = this;
    	var target = this.getTargetFromEvent(e);
    	if (!target||this.isMyself(target, data)) return;  
    	me.move(target, data, e);
    	//me.dvDraggable.moveShortcut(target,source);
    	//me.moveShortcut(target,source);
    }
});
