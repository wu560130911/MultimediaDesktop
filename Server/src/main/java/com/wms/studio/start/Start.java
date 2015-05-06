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

package com.wms.studio.start;

import com.alibaba.dubbo.container.Main;

/**
 * @author WMS
 * 
 */
public class Start {

	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		System.out.println("系统开始启动中...");
		System.out.println("系统启动时间:" + startTime);
		Main.main(args);
		long endTime = System.currentTimeMillis();
		System.out.println("系统启动完成时间:" + endTime);
		System.out.println("系统启动完成,总耗时:" + (endTime - startTime) + "ms.");
	}

}
