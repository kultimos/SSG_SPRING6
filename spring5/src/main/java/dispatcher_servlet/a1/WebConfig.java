package dispatcher_servlet.a1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
// @EnableConfigurationProperties注解表示将配置文件中的属性值绑定到指定的Java类中
@EnableConfigurationProperties({WebMvcProperties.class, ServerProperties.class})
public class WebConfig {

    /**
     * 对于这种支持内嵌web容器的配置类,有三项是必配的
     * 1. 内嵌web容器(tomcat)的工厂
     * 2. 创建DispatcherServlet
     * 3. 注册DispatcherServlet到tomcat容器中
     * 最终的这个DispatcherServlet需要依托于tomcat容器才能运行
     */

    @Bean   // 1.web容器工厂
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(ServerProperties serverProperties) {
        return new TomcatServletWebServerFactory(serverProperties.getPort());
    }

    @Bean  // 2.创建DispatcherServlet
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet,
                                                                               WebMvcProperties webMvcProperties) {
        DispatcherServletRegistrationBean registrationBean = new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        /**
         * 这里设置的数字表示优先级,在有多个DispatcherServlet时,可以通过该参数进行加载顺序的控制
         * 但只要该数字大于零,即表示在tomcat容器启动时就对DispatcherServlet进行初始化;
         */
        // registrationBean.setLoadOnStartup(1);

        // 通过读取绑定了配置文件中信息的类的属性,实现通过配置文件对Servlert初始化时机的控制
        registrationBean.setLoadOnStartup(webMvcProperties.getServlet().getLoadOnStartup());
        return registrationBean;
    }

}
