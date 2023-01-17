package com.teee.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Xu ZhengTao
 * @version 3.0
 * 拦截器 | 权限验证 | 前缀为 /authorization
 *
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截了捏" + request.getRequestURI());
        response.getWriter().write();
        return false;
    }

}
