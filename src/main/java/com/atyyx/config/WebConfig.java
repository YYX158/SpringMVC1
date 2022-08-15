package com.atyyx.config;

import com.atyyx.config.Interceptor.FirstInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.Thymeleaf;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.List;
import java.util.Properties;

/**
 * SpirngMVC的配置类
 * 我们这个类标识为配置类
 * 扫描组件、视图解析器、默认的Servlet、MVC注解驱动
 * 视图控制器、文件上传解析器、拦截器、异常解析器
 */
@Configuration
// 扫描组件
@ComponentScan("com.atyyx.config.controller")
// 开启MVC的注解驱动
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer
{
    /**
     * 默认的Servlet来处理静态资源
     * @param configurer
     */
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer)
    {
        configurer.enable();
    }

    /**
     * 配置我们的视图解析器
     * @param registry
     */
    public void addViewControllers(ViewControllerRegistry registry)
    {
        // 添加一个重定向的视图控制器
        registry.addViewController("/").setViewName("index");
    }

    /**
     *  @Bean注解可以将标识的方法的返回值作为bean进行管理
     *  bean的id为方法的方法名
     * @return
     */
    @Bean
    public CommonsMultipartResolver multipartResolver()
    {
        return new CommonsMultipartResolver();
    }

    /**
     * 拦截器的注册器
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry)
    {
        /**
         * /**标识所有请求
         */
         registry.addInterceptor(new FirstInterceptor()).addPathPatterns("/**");
    }

    /**
     * 配置异常解析器
     * @param resolvers
     */
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers)
    {
        //SpringMVC中给我们提供的默认的处理异常的异常解析器
        // 创建一个异常解析器
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
        //创建一个异常
        Properties properties = new Properties();
        // 以键值对的形式出现     键：出现的异常    值：要跳转到的逻辑视图
        properties.setProperty("java.lang.ArithmeticException","error");
        //将配置放入异常解析器
        exceptionResolver.setExceptionMappings(properties);
        // 设置异常在请求域中的属性名
        exceptionResolver.setExceptionAttribute("ex");
        // 将我们自己创建的异常解析器注册
        resolvers.add(exceptionResolver);
    }

    /**
     * 配置生成的模板解析器
     * @return
     */
    @Bean
    public ITemplateResolver templateResolver()
    {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();

        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(
                webApplicationContext.getServletContext()
        );
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        return templateResolver;
    }

    /**
     * 生成模板殷勤并且为模板引擎注入模板解析器
     * @param templateResolver
     * @return
     */
    @Bean
    public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver)
    {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }


    /**
     * 创建一个视图解析器，需要添加成bean
     * @param templateEngine
     * @return
     */
    @Bean
    public ViewResolver vieeResolver(SpringTemplateEngine templateEngine)
    {
        // 创建一个视图解析器
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        // 设置一下编码方式
        viewResolver.setCharacterEncoding("UTF-8");
         // 给模板引擎进行赋值
        viewResolver.setTemplateEngine(templateEngine);
        return viewResolver;
    }
}
