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
 
/**
 * 异步加载数据工具类
 */
Ext.define('Wdesktop.core.util.AjaxGetData', {
	PostData : function() {
		console.log(arguments);
		var data = {
			method : "POST",
			timeout : 10000,
		};

		if (arguments.length >= 1) {
			data['url'] = arguments[0];
		}

		if (arguments.length >= 2) {
			data['params'] = arguments[1];
		}

		if (arguments.length >= 3) {
			data['success'] = arguments[2];
		}

		if (arguments.length >= 4) {
			data['failure'] = arguments[3];
		}

		Ext.Ajax.request(data);
	},
	PostJsonData : function() {
		console.log(arguments);
		var data = {
			method : "POST",
			timeout : 10000,
		};

		if (arguments.length >= 1) {
			data['url'] = arguments[0];
		}

		if (arguments.length >= 2) {
			data['jsonData'] = arguments[1];
		}

		if (arguments.length >= 3) {
			data['success'] = arguments[2];
		}

		if (arguments.length >= 4) {
			data['failure'] = arguments[3];
		}

		Ext.Ajax.request(data);
	},
	PostAsyncData : function(url, data, successFunc, failureFunc) {
		var data = {
			method : "POST",
			timeout : 10000,
			async : false
		};

		if (arguments.length >= 1) {
			data['url'] = arguments[0];
		}

		if (arguments.length >= 2) {
			data['params'] = arguments[1];
		}

		if (arguments.length >= 3) {
			data['success'] = arguments[2];
		}

		if (arguments.length >= 4) {
			data['failure'] = arguments[3];
		}
		Ext.Ajax.request(data);
	}
});
