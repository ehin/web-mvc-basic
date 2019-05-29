package spring.mvc.dispatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.servlet.demo.DispactcherService;

@RestController
public class DispatcherController {

    @Autowired
    DispactcherService dispactcherService;

    @GetMapping("dispatcher")
    public String getServletName(){
        return "dispacher : " + dispactcherService.getServletName();
    }
}
