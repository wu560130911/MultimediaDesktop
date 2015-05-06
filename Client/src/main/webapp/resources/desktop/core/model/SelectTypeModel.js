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
 * ClassName 查询字段的实体
 * text : 查询的显示文本
 * value: 查询的字段名
 */
 Ext.define("Wdesktop.core.model.SelectTypeModel",{
 	extend:"Ext.data.Model",
 	fields:[
 		{name:"text",type:"string",srotable:true},
 		{name:"value",type:"string",srotable:true}
 	]
 });
