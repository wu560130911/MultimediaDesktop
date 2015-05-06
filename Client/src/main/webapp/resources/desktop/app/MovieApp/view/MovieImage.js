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
 
Ext.define('Wdesktop.app.MovieApp.view.MovieImage', {
    extend: 'Ext.Img',
    alias : "widget.movieimage",
    height: 180,
    width: 270,
    src:'MovieImages/MovieImage.png',
    page:0,
    baseUrl:'',

    initComponent: function() {
        var me = this;
        Ext.applyIf(me, {
            
        });
        me.callParent(arguments);
    },
    
    afterRender : function() {
		var me = this;
		me.getEl().on('contextmenu', function (e) {
            e.stopEvent();
        }, this);
		if(me.item.length != 0){
			me.setSrc(me.baseUrl+me.item[0].src);
		}
		if(me.item.length>1){
			Ext.Function.defer(me.updateTime, 100, me);
		}
		me.callParent();
	},
	
	updateTime : function() {
		var me = this;
		if(me.page+1>=me.item.length){
			me.page = -1;
		}
		me.page++;
		me.setSrc(me.baseUrl+me.item[me.page].src);
		me.timer = Ext.Function.defer(me.updateTime, 4000, me);
	}

});
