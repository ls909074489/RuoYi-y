package com.ruoyi.framework.config;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.http.StatViewServlet;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.framework.datasource.DynamicDataSource;

/**
 * druid 配置多数据源
 * 
 * @author ruoyi
 */
@Configuration
public class DruidConfig {
	@Bean
	@ConfigurationProperties("spring.datasource.druid.master")
	public DataSource masterDataSource() {
		return DruidDataSourceBuilder.create().build();
	}

	@Bean
	@ConfigurationProperties("spring.datasource.druid.slave")
	@ConditionalOnProperty(prefix = "spring.datasource.druid.slave", name = "enabled", havingValue = "true")
	public DataSource slaveDataSource() {
		return DruidDataSourceBuilder.create().build();
	}

	@Bean(name = "dynamicDataSource")
	@Primary
	public DynamicDataSource dataSource(DataSource masterDataSource, DataSource slaveDataSource) {
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DataSourceType.MASTER.name(), masterDataSource);
		targetDataSources.put(DataSourceType.SLAVE.name(), slaveDataSource);
		return new DynamicDataSource(masterDataSource, targetDataSources);
	}

	@Bean
	public ServletRegistrationBean druidServlet() {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
		servletRegistrationBean.setServlet(new StatViewServlet());
		servletRegistrationBean.addUrlMappings("/monitor/druid/*");
		servletRegistrationBean.addUrlMappings("/druid/*");
//		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
		Map<String, String> initParameters = new HashMap<>();
		initParameters.put("resetEnable", "false"); // 禁用HTML页面上的“Rest All”功能
//		initParameters.put("allow", "10.8.9.115"); // ip白名单（没有配置或者为空，则允许所有访问）
		initParameters.put("loginUsername", "admin"); // ++监控页面登录用户名
		initParameters.put("loginPassword", "admin"); // ++监控页面登录用户密码
		initParameters.put("deny", ""); // ip黑名单
		// 如果某个ip同时存在，deny优先于allow
		servletRegistrationBean.setInitParameters(initParameters);
		return servletRegistrationBean;
	}
	
}
