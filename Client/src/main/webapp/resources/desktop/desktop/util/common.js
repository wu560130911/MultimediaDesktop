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
 
var comm = new Ext.util.MixedCollection();
var mainApplication = null;
comm.add('wallpaper_path', 'resources/resources/wallpapers/blue.jpg');
comm.add('wallpaper_stretch', true);

Ext.Ajax.request({
	url : 'user/loginInfo.json',
	method : "POST",
	timeout : 10000,
	params : {},
	async : false,
	success : function(response,options){
		var userData = Ext.JSON.decode(response.responseText);
		if(userData!=null&&userData['user']!=null){
			//放到session中
			//sessionStorage.setItem('user_id',userData['user']['loginName']);
			//sessionStorage.setItem('user_name',userData['user']['name']);
			//sessionStorage.setItem('user_role',userData['user']['role']);
			//之前加载过，刷新内容
			comm.add('user_id', userData['user']['loginName']);
			comm.add('user_name', userData['user']['name']);
			comm.add('user_role', userData['user']['role']);
		}
		if(userData!=null&&userData['theme']!=null){
			comm.add('wallpaper_path', userData['theme']['path']);
			comm.add('wallpaper_id', userData['theme']['id']);
		}
	},
	failure : function(){
		
	}
});

if (localStorage['starthelpwindow'] != null) {
	comm.add('starthelpwindow', localStorage['starthelpwindow']);
} else {
	comm.add('starthelpwindow', 'true');
}
