package com.ruoyi.framework.util.applicationcontext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Service;

/**
 * spring管理环境中获取bean
 * 
 * @author yangzz
 */
@Service("springContextUtil")
public class SpringContextUtil implements ApplicationContextAware {
	
	private static Log logger = LogFactory.getLog(ApplicationContextAware.class);
	
	// Spring应用上下文环境
	private static ApplicationContext applicationContext;

	/**
	 * 实现ApplicationContextAware接口的回调方法，设置上下文环境
	 * 
	 * @param applicationContext
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextUtil.applicationContext = applicationContext;
		initEvent();
		logger.info("****************************init ApplicationContext successfully:" + applicationContext.getClass().getName());
	}

	/**
	 * @return ApplicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	
	private void initEvent() {
		List<ApplicationContextInit> list = getBeansOfType(ApplicationContextInit.class);
		if (list != null && list.size() > 0) {
			Collections.sort(list, new InitSort());
			list.forEach((ApplicationContextInit init) -> {
				init.init();
				logger.info("****************************Spring容器启动完成执行初始化方法完成:" + init.getClass().getName());
			});
		}
	}
	
	/**
	 * 获取对象
	 * 
	 * @param beanName
	 * @return Object
	 * @throws BeansException
	 */
	public static Object getBean(String beanName) throws BeansException {
		Object object = null;
		try {
			object = applicationContext.getBean(beanName);
		} catch (NullPointerException e) {
			logger.warn("Spring容器中找不到beanName:" + beanName);
		}
		return object;
	}
	
	/**
	 * 取Bean实例
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		T object = null;
		try {
			object = getApplicationContext().getBean(clazz);
		} catch (NullPointerException e) {
			logger.warn("Spring容器中找不到class:" + clazz.getName());
		}
		return object;
	}
	
	/**
	 * 动态注入Bean
	 * 
	 * @param beanName
	 * @param beanClass
	 * @param properties
	 */
	public static void registerBean(String beanName, Class<?> beanClass, Map<String, String> properties) {
		registerBean(beanName, beanClass, properties, null);
	}
	
	/**
	 * 动态注入Bean
	 * 
	 * @param beanName
	 * @param beanClass
	 * @param properties
	 * @param refs
	 */
	public static void registerBean(String beanName, Class<?> beanClass, Map<String, String> properties, Map<String, String> refs) {
		BeanDefinitionBuilder userBeanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
		if (properties != null && properties.size() > 0) {
			for (Map.Entry<String, String> entry : properties.entrySet()) {
				userBeanDefinitionBuilder.addPropertyValue(entry.getKey(), entry.getValue());
			}
		}
		if (refs != null && refs.size() > 0) {
			for (Map.Entry<String, String> entry : refs.entrySet()) {
				userBeanDefinitionBuilder.addPropertyReference(entry.getKey(), entry.getValue());
			}
		}
		DefaultListableBeanFactory defaultListableBeanFactory = getBeanFactory();
		removeBean(beanName);
		defaultListableBeanFactory.registerBeanDefinition(beanName, userBeanDefinitionBuilder.getRawBeanDefinition());
	}
	
	
	/**
	 * 动态删除Bean
	 * 
	 * @param beanName
	 */
	public static void removeBean(String beanName) {
		DefaultListableBeanFactory defaultListableBeanFactory = getBeanFactory();
		if (defaultListableBeanFactory.containsBean(beanName)) {
			defaultListableBeanFactory.removeBeanDefinition(beanName);
		}
	}
	
	/**
	 * 获取一个类型的所有的Bean对象列表
	 * 
	 * @param type
	 *            类型
	 * @return
	 * @throws BeansException
	 */
	public static <T> List<T> getBeansOfType(Class<T> type) throws BeansException {
		List<T> list = null;
		try {
			list = new ArrayList<T>(getApplicationContext().getBeansOfType(type).values());
			AnnotationAwareOrderComparator.sort(list);
		} catch (NullPointerException e) {
			logger.warn("Spring容器中找不到class:" + type.getName());
		}
		return list;
	}
	
	/**
	 * 发布一个事件，所有的实现了ApplicationListener的类都会接收到这个事件
	 * 
	 * @param event
	 *            事件
	 */
	public static void publishEvent(ApplicationEvent event) {
		getApplicationContext().publishEvent(event);
	}

	/**
	 * 获取bean工厂
	 * 
	 * @return
	 */
	public static DefaultListableBeanFactory getBeanFactory() {
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
		return defaultListableBeanFactory;
	}

}
