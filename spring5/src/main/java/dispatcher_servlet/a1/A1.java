package dispatcher_servlet.a1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebApplicationContext;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;

@Slf4j
public class A1 {

    public static void main(String[] args) {
        // AnnotationConfigServletWebServerApplicationContext是一个支持内嵌tomcat容器的Spring容器实现
        AnnotationConfigServletWebServerApplicationContext context =
                new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
    }

}
