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
package com.wms.studio.test;

import org.junit.Test;

public class TestNumber {

	@Test
	public void test() {
		
		int[] a = new int[]{-7,-6,-5,-4,-3,-2,-1,1,2,3,4,5,6,7};
		for(int i=0;i<a.length;i++){
			System.out.println("---------------"+a[i]+"-------------------");
			System.out.println(a[i]&(-4));
			System.out.println(a[i]&(-2));
			System.out.println(a[i]&(-1));
		}
		
	}

}
