package com.kul.a08;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class MyController {
    /**
     * 这里我们注入的多个bean都加了@Lazy,这是因为我们的Controller本身是单例,在单例中使用其他域就需要加@Lazy,不然下面三个bean就会有失效的问题
     */

    @Lazy
    @Autowired
    private BeanForRequest beanForRequest;

    @Lazy
    @Autowired
    private BeanForSession beanForSession;

    @Lazy
    @Autowired
    private BeanForApplication beanForApplication;

    @GetMapping(value = "/test", produces = "text/html")
    public String test(HttpServletRequest request, HttpSession session){
        ServletContext sc = request.getServletContext();
        String sb = "<ul>" +
                    "<li>" + "request scope" + beanForRequest + "<li>" +
                    "<li>" + "session scope" + beanForSession + "<li>" +
                    "<li>" + "application scope" + beanForApplication + "<li>" +
                    "<ul>";
        return sb;



    }
}
