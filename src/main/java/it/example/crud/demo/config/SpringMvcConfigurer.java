package it.example.crud.demo.config;

import it.example.crud.demo.logging.LoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringMvcConfigurer {

	@Bean
	public LoggingFilter loggingFilter() {

		return new LoggingFilter("userService");
	}

	@Bean
	public FilterRegistrationBean<LoggingFilter> loggingFilterRegistrationBean() {

		return new FilterRegistrationBean<>(loggingFilter());
	}

//@Bean
//	public FilterRegistrationBean<HeaderFilter> headerFilter(){
//		FilterRegistrationBean<HeaderFilter> registrationBean = new FilterRegistrationBean<>();
//
//		registrationBean.setFilter(new HeaderFilter());
//		registrationBean.addUrlPatterns("/users-service/*");
//		registrationBean.setOrder(1);
//
//		return registrationBean;
//	}

}
