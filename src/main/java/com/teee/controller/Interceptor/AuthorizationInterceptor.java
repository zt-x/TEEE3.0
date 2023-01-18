package com.teee.controller.Interceptor;

import com.alibaba.fastjson2.JSON;
import com.teee.controller.Project.Annoation.RoleCheck;
import com.teee.controller.Project.ProjectCode;
import com.teee.util.Jwt;
import com.teee.vo.R;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

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
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取注解内容
            RoleCheck rc = handlerMethod.getMethodAnnotation(RoleCheck.class);
            if (rc == null) {
                return true;
            }
            String token = request.getHeader("Authorization");
            int role = -1;
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            try{
                role = Jwt.getRole(token);
            }catch (IllegalArgumentException e){
                response.getWriter().write(JSON.toJSONString(new R(ProjectCode.CODE_EXCEPTION_TOKENILLEGAL, null,"Token异常, 请重新登陆")));
                return false;
            }
            if(Jwt.getRole(token) < rc.role().ordinal()){
                response.getWriter().write(JSON.toJSONString(new R(ProjectCode.CODE_EXCEPTION_BUSSINESS, null,"权限不足")));
                return false;
            }else{
                return true;
            }
        }else{
            return true;
        }
    }

}