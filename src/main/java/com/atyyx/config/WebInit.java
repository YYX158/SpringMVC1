package com.atyyx.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

public class WebInit extends AbstractAnnotationConfigDispatcherServletInitializer
{
    /**
     * 设置一个配置类带代替Spring的配置文件
     * @return
     */
    @Override
    protected Class<?>[] getRootConfigClasses()
    {
        return new Class[]{SpringConfig.class};
    }

    /**
     * 设置一个配置类来代替SpringMVC的配置文件
     * @return
     */
    @Override
    protected Class<?>[] getServletConfigClasses()
    {
        return new Class[]{WebConfig.class};
    }


    /**
     * 在这个类中自动配置了DispatcherServlet
     * 在配置DispatcherServlet的时候需要指明<url-pattern></url-pattern>
     * @return
     */
    @Override
    protected String[] getServletMappings()
    {
        return new String[]{"/"};
    }

    /**
     * 设置当前的过滤器
     * @return
     */
    @Override
    protected Filter[] getServletFilters()
    {
        //  创建编码过滤器
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        // 设置编码方式
        characterEncodingFilter.setEncoding("UTF-8");
        // 设置请求和相应的编码方式都是utf-8
        characterEncodingFilter.setForceEncoding(true);

        // 创建处理请求方式的过滤器
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        //return super.getServletFilters();
        return new Filter[]{characterEncodingFilter,hiddenHttpMethodFilter};
    }

}
