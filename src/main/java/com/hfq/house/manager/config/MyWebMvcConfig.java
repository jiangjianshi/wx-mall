package com.hfq.house.manager.config;

import com.hfq.house.manager.interceptor.LoginInterceptor;
import com.hfq.house.manager.interceptor.PageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;

/**
 * 
 * @author jjs
 *
 */
@Configuration
@EnableWebMvc
public class MyWebMvcConfig implements WebMvcConfigurer {

	@Resource
	private PageInterceptor pageInterceptor;

	@Resource
	private LoginInterceptor loginInterceptor;


	@Bean
	public WebMvcConfigurationSupport forwardToIndex() {
		return new WebMvcConfigurationSupport() {
			@Override
			public void addViewControllers(ViewControllerRegistry registry) {
				registry.addViewController("/").setViewName("forward:/index");
			}
		};
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setUseSuffixPatternMatch(false).setUseTrailingSlashMatch(false);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/stat/**").addResourceLocations("classpath:/stat/");
		registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");
	}


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor);
		registry.addInterceptor(pageInterceptor);
	}
}