package cn.tedu.store.configurer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.tedu.store.interceptor.LoginInterceptor;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer{

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		List<String> addPathPatterns = new ArrayList<String>();
		addPathPatterns.add("/user/**");
		addPathPatterns.add("/web/**");

		List<String> excludePathPatterns = new ArrayList<String>();
		excludePathPatterns.add("/user/reg.do");
		excludePathPatterns.add("/user/login.do");
		excludePathPatterns.add("/web/register.html");
		excludePathPatterns.add("/web/login.html");
		registry.addInterceptor(new LoginInterceptor())
		.addPathPatterns(addPathPatterns)
	    .excludePathPatterns(excludePathPatterns);

	}

}
