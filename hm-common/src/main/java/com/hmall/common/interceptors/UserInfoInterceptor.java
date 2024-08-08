package com.hmall.common.interceptors;

import cn.hutool.core.util.StrUtil;
import com.hmall.common.utils.UserContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: hmall
 * @description:
 * @author: zh
 * @create: 2024-08-08 17:08
 **/
//这个拦截器不需要拦截，全部放行只需要做用户信息获取即可
	//并且这个SpringMVC拦截器定义完毕之后，需要配置拦截器
public class UserInfoInterceptor implements HandlerInterceptor {
	//preHandle在Controller之前执行,获取登录用户信息，保存到threadlocal里
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 1.获取登录用户信息
		String userInfo = request.getHeader("user-info");
		// 2.判断是否获取了用户，因为有的请求是直接放行的，如果有，存入threadlocal
		if (StrUtil.isNotBlank(userInfo)) { //isNotBlank判断是否为null,还可以判断是否是空字符串 ，空格等
			// 不为空，保存到ThreadLocal UserContext内部就是ThreadLocal,提供了一些操作的方法
			UserContext.setUser(Long.valueOf(userInfo));//Long.valueOf 字符串转成long
		}
		// 3.放行
		return true;

	}

	//afterCompletion在Controller之后执行,等controller用户执行完了，清除threadlocal
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		//清理用户
		UserContext.removeUser();
	}

}
