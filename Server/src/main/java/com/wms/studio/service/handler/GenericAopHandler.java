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

package com.wms.studio.service.handler;

import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.wms.studio.annotations.HandlerAnnotationFactoryBean;
import com.wms.studio.annotations.HandlerPoint;
import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.service.handler.api.HandlerApi;
import com.wms.studio.service.handler.api.HandlerData;

/**
 * @author WMS
 * 
 */
@Component
@Aspect
public class GenericAopHandler {

	private static final Logger log = Logger.getLogger(GenericAopHandler.class);

	private static final String TAG = "execution(* com.wms.studio.api.service.*.*(..))";

	@Resource
	private HandlerAnnotationFactoryBean handlerAnnotationFactoryBean;

	public void setHandlerAnnotationFactoryBean(
			HandlerAnnotationFactoryBean handlerAnnotationFactoryBean) {
		this.handlerAnnotationFactoryBean = handlerAnnotationFactoryBean;
	}

	@Around(value = TAG)
	public Object process(ProceedingJoinPoint point) throws Throwable {

		String handlerName = getHandlerName(point);
		HandlerData data = null;

		if (StringUtils.isBlank(handlerName)) {
			log.debug("未为该方法配置任何处理业务"
					+ point.getSignature().getName());
		} else {
			data = new HandlerData();
			//data.put("args", point.getArgs());
			data.setArgs(point.getArgs());
			execute(handlerAnnotationFactoryBean.getBeforeHandlers(handlerName),
					data);
		}

		Object result = point.proceed();

		if (!StringUtils.isBlank(handlerName)) {
			//data.put("result", result);
			data.setResult(result);
			execute(handlerAnnotationFactoryBean.getAfterHandlers(handlerName),
					data);
		}

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void execute(List<HandlerApi> handlers, HandlerData data) {
		for (HandlerApi handler : handlers) {
			handler.excute(data);
		}
	}

	private String getHandlerName(ProceedingJoinPoint point) throws NoSuchMethodException {

		//Object object = point.getTarget();
		//Signature signature = point.getSignature();
		//String name = signature.getName();
		// Class<?>[] parameterTypes = ((MethodSignature) point.getSignature())
		// .getMethod().getParameterTypes();
		try {
			// Method method = object.getClass().getMethod(name,
			// parameterTypes);
			Method method = ((MethodSignature) point.getSignature())
					.getMethod();
			HandlerPoint handlerPoint = method
					.getAnnotation(HandlerPoint.class);
			if (handlerPoint != null) {
				return handlerPoint.handlerName();
			}
		} catch (SecurityException e) {
		}
		return null;
	}
}
