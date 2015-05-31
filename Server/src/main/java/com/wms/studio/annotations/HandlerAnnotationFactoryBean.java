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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import com.wms.studio.api.utils.StringUtils;
import com.wms.studio.service.handler.api.HandlerApi;

/**
 * @author WMS
 * 
 */
@SuppressWarnings("rawtypes")
public final class HandlerAnnotationFactoryBean {

	private static final Logger log = Logger
			.getLogger(HandlerAnnotationFactoryBean.class);

	private static final String RESOURCE_PATTERN = "/**/*.class";

	private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private final List<String> packagesList = new LinkedList<String>();

	private final List<TypeFilter> typeFilters = new LinkedList<TypeFilter>();

	@Autowired
	private ApplicationContext context;

	private static final ConcurrentHashMap<String, ConcurrentHashMap<HandlerScope, LinkedList<HandlerApi>>> handlersMap = new ConcurrentHashMap<String, ConcurrentHashMap<HandlerScope, LinkedList<HandlerApi>>>();

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	/**
	 * 构造函数
	 * 
	 * @param packagesToScan
	 *            指定哪些包需要被扫描,支持多个包"package.a,package.b"并对每个包都会递归搜索
	 * @param annotationFilter
	 *            指定扫描包中含有特定注解标记的bean,支持多个注解
	 */
	@SafeVarargs
	public HandlerAnnotationFactoryBean(String[] packagesToScan,
			Class<? extends Annotation>... annotationFilter) {
		if (packagesToScan != null) {
			for (String packagePath : packagesToScan) {
				this.packagesList.add(packagePath);
			}
		}
		if (annotationFilter != null) {
			for (Class<? extends Annotation> annotation : annotationFilter) {
				typeFilters.add(new AnnotationTypeFilter(annotation, false));
			}
		}
	}

	/**
	 * 将符合条件的Bean以Class集合的形式返回
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void init() {
		log.info("业务Handler注解工厂类，扫描系统中所有的业务处理类");
		beforeHandlerResource(handlersMap);
	}

	private void beforeHandlerResource(
			ConcurrentHashMap<String, ConcurrentHashMap<HandlerScope, LinkedList<HandlerApi>>> handlersMap) {
		try {

			if (!this.packagesList.isEmpty()) {
				for (String pkg : this.packagesList) {
					String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
							+ ClassUtils.convertClassNameToResourcePath(pkg)
							+ RESOURCE_PATTERN;
					Resource[] resources = this.resourcePatternResolver
							.getResources(pattern);
					MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(
							this.resourcePatternResolver);

					onHandlerResource(resources, readerFactory, handlersMap);

				}
			}
		} catch (IOException e) {
			log.fatal("扫描业务处理异常", e);
		}
	}

	private void onHandlerResource(
			Resource[] resources,
			MetadataReaderFactory readerFactory,
			ConcurrentHashMap<String, ConcurrentHashMap<HandlerScope, LinkedList<HandlerApi>>> handlersMap)
			throws IOException {

		if (resources == null || readerFactory == null) {
			return;
		}

		for (Resource resource : resources) {
			if (resource.isReadable()) {
				MetadataReader reader = readerFactory
						.getMetadataReader(resource);
				// 这儿可以抽象出来，但是因为本项目只需要一个注解扫描
				if (matchesEntityTypeFilter(reader, readerFactory)) {
					Map<String, Object> map = reader.getAnnotationMetadata()
							.getAnnotationAttributes(
									"com.wms.studio.annotations.Handler");
					if (map != null) {
						String handlerName = (String) map.get("handlerName");
						String beanName = (String) map.get("beanName");
						HandlerScope scope = (HandlerScope) map.get("scope");
						if (scope == HandlerScope.None) {
							continue;
						}

						addHandler(handlerName, beanName, scope, handlersMap);
					}
				}
			}
		}
	}

	private void addHandler(
			String handlerName,
			String beanName,
			HandlerScope scope,
			ConcurrentHashMap<String, ConcurrentHashMap<HandlerScope, LinkedList<HandlerApi>>> handlersMap) {

		if (StringUtils.isBlank(beanName) || StringUtils.isBlank(handlerName)) {
			return;
		}

		HandlerApi handlerBean = context.getBean(beanName, HandlerApi.class);

		if (handlerBean == null) {
			log.fatal("请检查处理标识，未找到该处理类:" + handlerName);
			return;
		}

		ConcurrentHashMap<HandlerScope, LinkedList<HandlerApi>> handlerNameHandler = handlersMap
				.get(handlerName);

		if (handlerNameHandler == null) {
			handlerNameHandler = new ConcurrentHashMap<HandlerScope, LinkedList<HandlerApi>>();
			handlersMap.put(handlerName, handlerNameHandler);
		}

		LinkedList<HandlerApi> handlerApiBeans = handlerNameHandler.get(scope);

		if (handlerApiBeans == null) {
			handlerApiBeans = new LinkedList<HandlerApi>();
			handlerNameHandler.put(scope, handlerApiBeans);
		}

		handlerApiBeans.add(handlerBean);
	}

	/**
	 * 检查当前扫描到的Bean含有任何一个指定的注解标记
	 * 
	 * @param reader
	 * @param readerFactory
	 * @return
	 * @throws IOException
	 */
	private boolean matchesEntityTypeFilter(MetadataReader reader,
			MetadataReaderFactory readerFactory) throws IOException {
		if (!this.typeFilters.isEmpty()) {
			for (TypeFilter filter : this.typeFilters) {
				if (filter.match(reader, readerFactory)) {
					return true;
				}
			}
		}
		return false;
	}

	public List<HandlerApi> getBeforeHandlers(String handlerName) {

		return getHandlers(handlerName, HandlerScope.Before);
	}

	public List<HandlerApi> getAfterHandlers(String handlerName) {
		return getHandlers(handlerName, HandlerScope.After);
	}

	public List<HandlerApi> getHandlers(String handlerName, HandlerScope scope) {

		if (StringUtils.isBlank(handlerName) || scope == null
				|| HandlerScope.None.equals(scope)) {
			return Collections.emptyList();
		}
		ConcurrentHashMap<HandlerScope, LinkedList<HandlerApi>> handlerNameHandler = handlersMap
				.get(handlerName);
		if (handlerNameHandler == null) {
			return Collections.emptyList();
		}

		LinkedList<HandlerApi> handlerApis = handlerNameHandler.get(scope);

		if (handlerApis == null) {
			return Collections.emptyList();
		}

		return Collections.unmodifiableList(handlerApis);
	}

	public synchronized void refresh() {
		final ConcurrentHashMap<String, ConcurrentHashMap<HandlerScope, LinkedList<HandlerApi>>> tempHandlersMap = new ConcurrentHashMap<String, ConcurrentHashMap<HandlerScope, LinkedList<HandlerApi>>>();
		beforeHandlerResource(tempHandlersMap);
		handlersMap.clear();
		handlersMap.putAll(tempHandlersMap);
	}
}
