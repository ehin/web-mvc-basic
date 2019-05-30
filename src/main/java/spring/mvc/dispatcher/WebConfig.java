package spring.mvc.dispatcher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@EnableWebMvc
@Configuration
@ComponentScan(useDefaultFilters = false, includeFilters = @ComponentScan.Filter(Controller.class))
public class WebConfig  implements WebMvcConfigurer{
    // 3-1 ServletDispatcher의 Default Bean 을 사용자 정의해서 쓰기.

    // 3-1 SpringMvc : Direct Setting
    // HandlerMapping : Interseptor , handler의 우선순위 일일히 설정&커스텀  --> 사실상 잘안함.
    @Bean
    public HandlerMapping handlerMapping(){
        RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        // requestMappingHandlerMapping.setInterceptors();
        // requestMappingHandlerMapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return requestMappingHandlerMapping;
    }

    // 3-1 SpringMvc : Direct Setting
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

    // 3-1 SpringMvc : Direct Setting, custom
    // ViewResolver 커스텀 : 화면은 WEB-INF만. jsp형식으로.
/*    @Bean
    public ViewResolver viewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }*/

    // 3-2 SpringMvc : @EnbaleWebMvc 로 셋팅 및 커스텀
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        // 원하는 타입으로만 받을 수 있도록 - 복잡한 설정인데, SpringBoot는 간단하게 함.
        // registry.enableContentNegotiation();
        registry.jsp("/WEB-INF/","jsp");
    }

}
