package spring.mvc.dispatcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@Configuration
@ComponentScan(useDefaultFilters = false, includeFilters = @ComponentScan.Filter(Controller.class))
public class WebConfig {
    // ServletDispatcher의 Default Bean 을 사용자 정의해서 쓰기.

    // HandlerMapping : Interseptor , handler의 우선순위 일일히 설정&커스텀  --> 사실상 잘안함.
    @Bean
    public HandlerMapping handlerMapping(){
        RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        // requestMappingHandlerMapping.setInterceptors();
        requestMappingHandlerMapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return handlerMapping();
    }

    // HandlerAdapter 일일히 설정&커스텀 --> 사실상 잘안함.
    @Bean
    public HandlerAdapter handlerAdapter(){
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        // adapter.setArgumentResolvers(); - Controller에서 파라미터 지정할 때 사용
          // @PathVaraible - hello/{id}  , @RequestParam String name - hello/1?name=mvc
          //  @ModelAttribute User use - 객체 hello/1?name=mvn&time=2
        // adapter.setMessageConverters()-  @RequestBody String body - 요청 메세지
        return  adapter;
    }

    // ViewResolver 커스텀 : 화면은 WEB-INF만. jsp형식으로.
    @Bean
    public ViewResolver viewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
}
