package spring.mvc.dispatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import spring.servlet.demo.DispactcherService;

@Controller
public class DispatcherController {

    @Autowired
    DispactcherService dispactcherService;

    // annotation기반 핸들러 - Request..handler Adaopter
    @GetMapping("dispatcher")
    @ResponseBody
    public String getServletName(){
        return "dispacher : " + dispactcherService.getServletName();
    }

    @GetMapping("/sample")
    public String sample(){
        return "/WEB-INF/sample.jsp";
    }
}
