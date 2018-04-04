package com.orilore.config;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.orilore.util.RedisDAO;

/**
 * SpringBoot 跨域配置
 * @author yue
 */
@Configuration
public class CorsConfig {
	@Resource
	private RedisDAO redis;
	/**
	 * 构建配置对象
	 * @return Configuration
	 */
	private CorsConfiguration buildConfig() {
		CorsConfiguration config = new CorsConfiguration();
        //允许来自所有域的请求进入
		config.addAllowedOrigin("*");
        //允许请求携带所有头信息
        config.addAllowedHeader("*");
        //允许所有请求方法进入
        config.addAllowedMethod("*");
        //允许向客户端响应token头信息
        config.addExposedHeader("token");
        return config;
    }
	
	/**
	 * 配置跨域过滤器
	 * @return Filter
	 */
    @Bean
    public CorsFilter corsFilter() {
    	//存储request与跨域配置信息的容器
    	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); 
        return new CorsFilter(source);
    }
    /**
     * 配置登录过滤器
     * @return Filter
     */
    @Bean
    public Filter loginFilter(){
    	//该过滤器在请求开始处理之前先执行
    	return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
					throws ServletException, IOException {
				response.setContentType("text/html;charset=utf-8");
				String url = request.getRequestURL().toString();
				//判断是否为登录请求，如果是则不检查token
				if(url.indexOf("login.do")>0){
					chain.doFilter(request, response);
				}else{
					String key = request.getHeader("token");
					//如果请求头中有token
					if(key!=null){
						//检查redis数据库中是否存在token对应的键值对
						if(redis.select(key)!=null){
							redis.update(key);//更新键值对有效期
							chain.doFilter(request, response);
						}else{
							response.getWriter().write("{\"code\":\"120\",\"text\":\"登录超时\"}");
						}
					}else{
						response.getWriter().write(("{\"code\":\"110\",\"text\":\"用户身份识别失败\"}"));
					}
				}
			}
		};
    }
    /**
     * 配置字符编码过滤器
     * @return Filter
     */
    @Bean
    public Filter characterEncodingFilter() {
    	CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");  
        filter.setForceEncoding(true);  
        return filter;  
    }  
}