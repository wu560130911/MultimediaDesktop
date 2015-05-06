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

package com.wms.studio.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author WMS
 * 业务处理标记
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Handler {

	/**
	 * 业务处理标识
	 * @return
	 */
	String handlerName();

	/**
	 * 业务处理Bean名称，必须是Spring容器里注册的名称
	 * @return
	 */
	String beanName();

	/**
	 * 业务处理的位置
	 * @return
	 */
	HandlerScope scope() default HandlerScope.None;
}
