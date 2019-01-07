package cn.tedu.store.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		System.out.println("LoginInterceptor.preHandle()");

		// 拦截规则
		/*
		 * 如果为登录，重定向到登录，并拦截 如果已登录，直接放行
		 */
		HttpSession session = request.getSession();
		if (session.getAttribute("uid") == null) {
			response.sendRedirect("../web/login.html");
			return false;
		}
		// 返回值:true=放行 ,false=拦截
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		System.out.println("LoginInterceptor.postHandle()");

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		System.out.println("LoginInterceptor.afterCompletion()");

	}

}
