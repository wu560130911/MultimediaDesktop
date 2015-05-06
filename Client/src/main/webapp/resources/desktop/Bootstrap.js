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
 
if(window.WMSDesktop){
	window.WMSDesktop = undefined;
}
window.WMSDesktop = {
	
};

WMSDesktop.Bootstrap = {
	
	SHORTCUTMARK : "<link rel='shortcut icon' href='{url}' />",
	
	CSSMARK : "<link rel='stylesheet' id='{id}' type='text/css' href='{url}' />",
	
	JAVASCRIPTMARK : "<script type='text/javascript' src='{url}' onload='WMSDesktop.Bootstrap.onScriptLoad()'></script>",
	
	pendFiles : 0,
	
	onScriptLoad : function(){
		WMSDesktop.Bootstrap.pendFiles--;
		if(WMSDesktop.Bootstrap.pendFiles == 0){
			WMSDesktop.Bootstrap.loadCallback.call(this);
		}
	},
	
	resTypes : {
		CSS : 'css',
		JAVASCRIPT : 'javascript',
		SHORTCUT : 'shortcut'
	},
	
	loadRequires : function(res){
		for(var i = 0;i < res.length;i++){
			var resource = res[i];
			if(resource.type == WMSDesktop.Bootstrap.resTypes.CSS){
				WMSDesktop.Bootstrap.loadCSS(resource);
			}else if(resource.type == WMSDesktop.Bootstrap.resTypes.JAVASCRIPT){
				WMSDesktop.Bootstrap.loadJavaScript(resource);
				
			}else if(resource.type == WMSDesktop.Bootstrap.resTypes.SHORTCUT){
				WMSDesktop.Bootstrap.loadShortcut(resource);
			}
		}
	},
	
	loadCSS : function(css){
		document.write(WMSDesktop.Bootstrap.CSSMARK.replace('{id}',css.id).replace('{url}',css.url));
	},
	
	loadJavaScript : function(script){
		WMSDesktop.Bootstrap.pendFiles ++ ;
		var js = document.createElement('script');
		js.src = script.url;
		js.onload = WMSDesktop.Bootstrap.onScriptLoad;
		js.onreadystatechange = function(){
			if(js.readyState == 'loaded'){
				WMSDesktop.Bootstrap.onScriptLoad();
			}
		};
		document.getElementsByTagName('head')[0].appendChild(js);
		//document.write(WMSDesktop.Bootstrap.JAVASCRIPTMARK.replace('{url}',script.url));
	},
	
	loadShortcut : function(shorcut){
		document.write(WMSDesktop.Bootstrap.SHORTCUTMARK.replace('{url}',shorcut.url));
	}
};
