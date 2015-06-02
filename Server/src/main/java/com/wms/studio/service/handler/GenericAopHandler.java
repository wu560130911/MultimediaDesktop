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
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.wms.studio.annotations.HandlerAnnotationFactoryBean;
import com.wms.studio.annotations.HandlerPoint;
import com.wms.studio.annotations.HandlerScope;
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

	@Resource
	private ApplicationContext applicationContext;

	public void setHandlerAnnotationFactoryBean(
			HandlerAnnotationFactoryBean handlerAnnotationFactoryBean) {
		this.handlerAnnotationFactoryBean = handlerAnnotationFactoryBean;
	}

	/**
	 * @param applicationContext
	 *            the applicationContext to set
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * TODO 需要继续优化代码
	 * 
	 * @param point
	 * @return
	 * @throws Throwable
	 */
	@Around(value = TAG)
	public Object process(ProceedingJoinPoint point) throws Throwable {

		String handlerName = getHandlerName(point);
		boolean isNeedHandler = StringUtils.isNotBlank(handlerName);
		HandlerData data = null;
		Object result = null;
		boolean handlerResult = false;

		if (isNeedHandler) {
			data = new HandlerData();
			data.setArgs(point.getArgs());
			handlerResult = beforeProcess(data, handlerName);
		}

		if (handlerResult) {
			return null;
		}

		try {
			result = point.proceed();
		} catch (Exception e) {
			if (isNeedHandler) {
				data.setException(e);
				handlerResult = exceptionProcess(data, handlerName);
			} else {
				throw e;
			}
		}

		if (isNeedHandler) {
			data.setResult(result);
			handlerResult = afterProcess(data, handlerName);
		}

		return result;
	}

	private boolean beforeProcess(HandlerData data, String handlerName) {
		return execute(
				handlerAnnotationFactoryBean.getBeforeHandlers(handlerName),
				data);
	}

	private boolean afterProcess(HandlerData data, String handlerName) {
		return execute(
				handlerAnnotationFactoryBean.getAfterHandlers(handlerName),
				data);
	}

	private boolean exceptionProcess(HandlerData data, String handlerName) {
		return execute(handlerAnnotationFactoryBean.getHandlers(handlerName,
				HandlerScope.Exception), data);
	}

	@SuppressWarnings({ "unchecked" })
	private boolean execute(List<String> handlers, HandlerData data) {
		boolean flag = false;
		for (String handler : handlers) {

			HandlerApi<HandlerData> handlerBean = applicationContext.getBean(
					handler, HandlerApi.class);

			if (handlerBean == null) {
				log.fatal("未在容器里找到对应的处理逻辑类，handlerBeanName=" + handler);
				continue;
			}

			handlerBean.excute(data);
			flag = flag || handlerBean.isAbort();
		}
		return flag;
	}

	private String getHandlerName(ProceedingJoinPoint point)
			throws NoSuchMethodException {

		// Object object = point.getTarget();
		// Signature signature = point.getSignature();
		// String name = signature.getName();
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
