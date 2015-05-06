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
 
Ext.define('Wdesktop.app.UserManager.model.OnlineUserModel', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id',
		type : 'string',
		srotable : true
	}, {
		name : 'username',
		type : 'string',
		srotable : true
	}, {
		name : 'host',
		type : 'string',
		srotable : true
	}, {
		name : 'startTime',
		type : 'string',
		srotable : true
	}, {
		name : 'lastAccessTime',
		type : 'string',
		srotable : true
	}, {
		name : 'userid',
		type : 'string',
		srotable : true
	}, {
		name : 'isLogin',
		type : 'boolean',
		srotable : true
	}]
});
