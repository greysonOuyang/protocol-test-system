package com.yuyi.pts.common.cros;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 本地8080端口全局允许跨域
 *
 * @author greyson
 * @since 2021/4/27
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     *  允许跨域访问
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 可限制哪个请求可以通过跨域
        registry.addMapping("/**")
                // 可限制固定请求头可以通过跨域
                .allowedHeaders("*")
                // 可限制固定methods可以通过跨域
                .allowedMethods("*")
                // 可限制访问ip可以通过跨域
                .allowedOrigins("http://localhost:8080")
                // 是否允许发送cookie
                .allowCredentials(true)
                .exposedHeaders(HttpHeaders.SET_COOKIE);
    }

}
