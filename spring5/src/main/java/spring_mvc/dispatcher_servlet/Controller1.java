package spring_mvc.dispatcher_servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class Controller1 {

    @GetMapping("/test1")
    public ModelAndView test1() throws Exception {
        log.info("test1()");
        return null;
    }

    @PostMapping("/test2")
    public ModelAndView test2(@RequestParam("name") String name) throws Exception {
        log.info("test2({})", name);
        return null;
    }
//
//    @PostMapping("/test3")
//    public ModelAndView test3(@Token String token) {
//        log.info("test3({})", token);
//        return null;
//    }
//
//    @RequestMapping("/test4.yml")
//    @Yml
//    public User test4() {
//        log.info("test4");
//        return new User("张三", 18);
//    }

    public static class User{
        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    public static void main(String[] args) {

    }
}
