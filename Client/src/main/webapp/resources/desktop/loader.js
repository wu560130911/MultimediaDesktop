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
 
//改造窗体的层次
Ext.override(Ext.ZIndexManager, {
	tempHidden : [],
	show : function() {
		var comp, x, y;
		while (comp = this.tempHidden.shift()) {
			x = comp.x;
			y = comp.y;
			comp.show();
			comp.setPosition(x, y);
		}
	}
});
Ext.Loader.setConfig({
	enabled : true,
	paths : {
		'Ext.ux' : 'resources/resources/extjs/ux',
		'Wdesktop':'resources/desktop'
	}
});
Ext.require([ 'Wdesktop.desktop.system.App']);
var myDesktopApp;

if(!returnCitySN){
	var returnCitySN = {
			'cip' : '10.28.20.100',
			'cname' : '江西省赣州市'
		};
}

var wmsmodulx = [], wmsmoduly = [];
var startLoadingTime = new Date().getTime();

Ext.onReady(function() {
	Ext.MessageBox.show({
        title : "正在加载桌面, 请稍候...",
        msg: "<br/>加载系统基础组件....",
        progressText: "20%",
        width:300,
        progress:true,
        closable:false,
        icon:"ext-mb-download"
    });
	Ext.MessageBox.updateProgress(0.2);
	myDesktopApp = new Wdesktop.desktop.system.App();
	window.setTimeout(function(){
    	Ext.MessageBox.hide();
    },1000);
});
