package com.teee.config;

import com.teee.project.Interceptor.AuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcSupport implements WebMvcConfigurer {

    @Autowired
    AuthorizationInterceptor authorizationInterceptor;
    @Value("${path.pic.works}")
    private String worksPicPath;

    @Value("${path.pic.faces}")
    private String facesPicPath;

    @Value("${path.file.files}")
    private String filePath;

    @Value("${path.file.temps}")
    private String tempsPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/pic/works/**").addResourceLocations("file:" + worksPicPath);
        registry.addResourceHandler("/pic/faces/**").addResourceLocations("file:" + facesPicPath);
        registry.addResourceHandler("/file/files/**").addResourceLocations("file:" + filePath);
        registry.addResourceHandler("/file/temps/**").addResourceLocations("file:" + tempsPath);
    }
}
