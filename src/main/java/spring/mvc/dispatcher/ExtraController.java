package spring.mvc.dispatcher;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@org.springframework.stereotype.Controller("/extra")
public class ExtraController implements Controller {
    // modelAndView에 화면 실어주기.: simplehandler가 핸들링. Internal view resolver가 화면 찾아줌.
    @Override
    public ModelAndView handleRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        return new ModelAndView("/WEB-INF/extra.jsp");
    }
}
