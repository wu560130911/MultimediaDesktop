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
 
Ext.define('Wdesktop.app.MovieApp.model.MovieModel', {
    extend: 'Ext.data.Model',
    fields: [{
    	name:'id',
    	type:'int',
    	srotable:true
    },{
        name: 'title',
        type: 'string',
        srotable:true
    },{
        name: 'author',
        type: 'string',
        srotable:true
    },{
        name: 'actor',
        type: 'string',
        srotable:true
    },{
        name: 'type',
        type: 'string',
        srotable:true
    },{
        name: 'duration',
        type: 'int',
        srotable:true
    },{
        name: 'size',
        type: 'int',
        srotable:true
    },{
        name: 'description',
        type: 'string',
        srotable:true
    },{
        name: 'madetime',
        type: 'string',
        srotable:true
    },{
    	name: 'time',
        type: 'string',
        srotable:true
    },{
    	name: 'userId',
        type: 'string',
        srotable:true
    },{
    	name: 'filename',
        type: 'string',
        srotable:true
    }]
});
