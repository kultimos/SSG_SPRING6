package spring_mvc.dispatcher_servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;

@Slf4j
public class A1 {

    public static void main(String[] args) throws Exception {
        // AnnotationConfigServletWebServerApplicationContext是一个支持内嵌tomcat容器的Spring容器实现
        AnnotationConfigServletWebServerApplicationContext context =
                new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);

        // 这个Map即路径与控制器方法之间的映射关系
        // Map中的key为路径、请求方式等信息,Map中的value为控制器方法
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        handlerMethods.forEach((k,v) -> {
            System.out.println(k + "  :  " + v);
        });

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/test2");
        request.setParameter("name", "占");
        // 通过一个Mock请求,获取处理器执行链对象
        HandlerExecutionChain chain = handlerMapping.getHandler(request);
        System.out.println(chain);

        System.out.println("==================>");
        MyRequestMappingHandlerAdapter handlerAdapter = context.getBean(MyRequestMappingHandlerAdapter.class);
        handlerAdapter.invokeHandlerMethod(request, new MockHttpServletResponse(), (HandlerMethod)chain.getHandler());

        // 获取参数解析器
        List<HandlerMethodArgumentResolver> argumentResolvers = handlerAdapter.getArgumentResolvers();


    }

}
