Spring Web MVC Basic
---------------

# 1. 동작원리
- view : Thymeleaf, JSP
- webflux : X
- Srping Boot

## 1-1 스프링MVC소개
- Controller URI 맵핑
- 4.3이전 ` @RequestMapping(value = "/events", method = RequestMethod.GET)` 
  - 4.3 이후 , `@GetMapping("/events")`

- Controller : URI매핑 받아와서 model에 service주입받아서 추가. 하고 view연결
- Service : Data받는 로직처리
- View : 데이터 보여주기

## 1-2 서블릿이란?
- HttpServlet 클래스가 중요
- 쓰레드를 만들어서 요청을 처리함으로 빠르고, 플랫폼 독립적.
- 서블릿은 서블릿 컨테이너가 실행. 
- 생명주기 : init(), doGet(res, req), destory()
- 초기화는 한번뿐, doGet혹은 doPost로 계속 요청이 들어올때마다 계쏙 이용.

## 1-3 서블릿 애플리케이션 샘플예제.
- intellj에서 maven프로젝트 `maven-archetype-webapp`만들기.
- pom.xml에 javax.servlet-api 추가
- extends HttpSerlvet해서 doGet메소드 구현
- Run configure에 tomcat추가 및 Deployment에 war exploded로 지정.

## 1-4 서블릿 리스너와 서블릿 필터.
- 서블릿 리스너 : 웹 애플리케이션에서 발생하는 주요 이벤트 감지 및 변경
  - 서블릿 컨텍스트 수준의 이벤트
    - 컨텍스트 라이프사이클 이벤트(implements ServletContextListner)
    - 컨텍스트 애트리뷰트 변경 이벤트
  - 세션 수준의 이벤트
    - 세션 라이프사이클 이벤트
    - 세션 애트리뷰트 변경 이벤트

- 리스너구현
```
public class MyListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Context init");
        sce.getServletContext().setAttribute("name","listener");
    }
```
- web.xml에추가
```
  <listener>
    <listener-class>MyListener</listener-class>
  </listener>
```
- servlet 클래스에서 사용
```
 resp.getWriter().println("<h1>Hi I am a " + getName() +"servlet</h1>");

    private Object getName() {
        return getServletContext().getAttribute("name");
    }
```

- 서블릿 필터 : doGet, doPost등 응답하기 전에 추가적인 작업을 할 수 있음. 체인형태로 추가적으로 적용됨. (implements Filter)

- Filter구현 
```
public class MyFilter implements Filter {
   @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Filter doFilter");
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
```
- web.xml에 필터등록
```
  <filter>
    <filter-name>Myfilter</filter-name>
    <filter-class>MyFilter</filter-class>
  </filter>

  <filter-mapping>
    <filter-name>Myfilter</filter-name>
    <servlet-name>hello</servlet-name>
  </filter-mapping>
```

- 서블릿 필터와 리스너 호출순서
  - 리스너초기화 ->필터 초기화->서블릿 초기화 -> do필터 -> 서블릿 doGet -> 서블릿 destory -> 필터 destory -> 리스너 destory

## 2-1 스프링 Ioc컨테이너 연동하기
- srping-webmvc pom.xml에 의존성 추가.
- web.xml에 스프링이 제공해주는 리스너 추가. 스프링 어플리케이션 컨텍스트를 만들어서 서블릿 컨텍스트에 등록해줌. `ContextLoaderListener`
```
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>
```
- 즉, 빈 객체를 스프링 리스너를 통해 컨텍스트 로더에 등록하는 일. 
-  요즘은 XML보다 JAVA설정파일이 대부분.
- contextClass의 AnnotationConfigWebApplicationContext라는 스프링 어플리케이션 컨텍스트를 이용할꺼임.
- 자바파일 설정 위치는 contextConfigLocation 이름으로, AppConfig 클래스를 지정. - 빈이 될 파일.
```
 <context-param>
    <param-name>contextClass</param-name>
    <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
  </context-param>

  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>AppConfig</param-value>
  </context-param>
```
- AnnotationConfigWebApplicationContext 에 Appconfig를 빈으로 설정해줌.
- 빈으로  설정한 파일 사용법 : `ContextLoaderListener`> `ContextLoader` 에 들어가보면, 초기화하는 곳에 `WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,()`를 이용하고 있음을 알 수 있음.
  ```
  WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
    ```
### 여러 서블릿 공통으로 처리하기
- front controller : 모든요청을 하나로 받고 - Handler로 분배(dispatch) 
- 이러한 역할을 하는 것이 스프링의 DispatcherServlet.
#### Root Web ApplicationContext: Service, Repository
- 주로 다른 서블릿 컨텍스트들과 공유해서 쓸 수 있음.
#### Servlet Web Application Servlet 
- Root Web applicationContext를 상속 받음. 주로 web과 관련된 Controller, ViewResolver, HandlerMapping 해당 dispacterServlet에 한정된 것들.
#### DispatcherServlet : 
- dispatcherServlet은 공유해서 쓸 수가 없음.

## 2-2 스프링 MVC Root Context와 Web Context
- 계층구조 만들기
  - Root ContextLoader :Service 
  - WebApplicationContext : Controller.
- dispatcherServlet 한개에 모든 컨텍스트를 등록하는 쪽으로 바뀌고 있음.
-  서블릿 컨테이너안에 -> 서블릿 애플리케이션을 등록 (리스너, 디스패처 컨텍스트)
-  스프링 부트 : 스프링부트 애플리케이션이 먼저 뜨고, 안에 톰캣이 있고. 서블릿은 톰캣 안에 스프링을 코드로 등록함.

## 2-3 dispatcher Servlet  동작원리 @ResponseBody지정 후 
- HandlerMapping: 핸들러를 찾아주는 인터페이스 -default 2개 
  - BeanNameUrlHanderMapping
  -  RequestMappingHandlerMapping (annotation 핸들러)
- HandlerAdapter: 핸들러를 실행하는 인터페이스 -default3개
  - HttpRequestHandlerAdapter
  - SimpleControllerHandlerAdapter
  - RequestMappingHandlerAdapter
- invokeHandlerMethod로 @GetMapping지정한 메소드 부름.
- HandlerMethodReturnValueHandler 의 종류 15가지 리턴값을 처리해줌.

## 2-4 Dispatcher Servlet  동작원리 @ResponseBody지정안하고, View가있는 경우
- 1.요청을 분석한다. (로케일, 테마, 멀티파트 등) 
- 2.(핸들러 맵핑에게 위임하여) 요청을 처리할 핸들러를 찾는다. 
- 3.(등록되어 있는 핸들러 어댑터 중에) 해당 핸들러를 실행할 수 있는 “핸들러 어댑터”를 찾는다. 
- 4.찾아낸 “핸들러 어댑터”를 사용해서 핸들러의 응답을 처리한다. 
  - 핸들러의 리턴값을 보고 어떻게 처리할지 판단한다. 
  - 뷰 이름에 해당하는 뷰를 찾아서 모델 데이터를 랜더링한다. 
  - @ResponseEntity가 있다면 Converter를 사용해서 응답 본문을 만들고. 
- 5.(부가적으로) 예외가 발생했다면, 예외 처리 핸들러에 요청 처리를 위임한다.
- 6.최종적으로 응답을 보낸다.

## 2-5 Dispatcher Controller
- DispatcherServlet.properties에 기본적인 인터페이스 들이 들어있음.

## 2-6 스프링 MVC의 기본적인 인터페이스들
- multipartResolver : 파일업로드에 사용.
- LocaleResolver : client의 지역정보를 확인해서 MessageSource등을 키값을 리졸빙 함. `accept-language`
- ThemeResolver : 테마 바꾸기. 
- HandlerMapping : 요청 처리할 핸들러.
- HandlerAdapter : MVC확장의 핵심. 핸들러를 원하는대로 만들 수 있음.
- HandlerExceptionResolver : 에러처리 (@EXceptionResolver.)
- RequestToViewNameTranslator: 뷰이름이 없는 경우 요청이름으로 알아서 찾아줌. 메소드이름.
-  ViewResolver : 뷰이름으로 실제 뷰를 찾아내는 인터페이스 RequestToViewNameTranslator와 같이 찾음.
- FlashMapManager : 리다이렉트를 할때 데이터가 또 넘어오는걸 방지하기 위해서, Get를 함. 중복 Form submit을 방지. 예 ) redirect:/events/id?201912
혹은 request parm없이 데이터를 건내주기도 함.

## 2-7. Spring MVC동작원리
- 결국엔 Servlet (DispatcherServlet)
- DispatcherServlet 초기화
  - 1.특정 타입에 해당하는 빈을 찾는다. 
  - 2.없으면 기본 전략(DispatcherServlet.properties) 사용
- 스프링 부트 사용하지 않는 스프링 MVC 
  - 서블릿 컨네이너(ex, 톰캣)에 등록한 웹 애플리케이션(WAR)에 DispatcherServlet을 등록한다. 
  - web.xml에 서블릿 등록 
  ```
  <servlet>
    <servlet-name>app</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <param-name>contextClass</param-name>
      <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
    </init-param>
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>spring.mvc.dispatcher.WebConfig</param-value>
    </init-param>
  </servlet>
  <servlet-mapping>
    <servlet-name>app</servlet-name>
    <url-pattern>/app/*</url-pattern>
  </servlet-mapping>
  ```
  
  - WebApplicationInitializer에 자바 코드로 서블릿 등록 (스프링 3.1+, 서블릿 3.0+)
```
// web.xml없이 dispatcherServlet등록하기
public class WebApplicationInit implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(WebConfig.class);
        context.refresh();

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        ServletRegistration.Dynamic app = servletContext.addServlet("app", dispatcherServlet);
        app.addMapping("/app/*");
    }
}
```

 
- 스프링 부트를 사용하는 스프링 MVC 
  - 자바 애플리케이션에 내장 톰캣을 만들고 그 안에 DispatcherServlet을 등록한다. 
  - 스프링 부트 자동 설정이 자동으로 해줌. 
  - 스프링 부트의 주관에 따라 여러 인터페이스 구현체를 빈으로 등록한다. 
 
 

 # 3. 스프링 MVC설정
 ## 3-1 Direct Setting
 - 스프링 기본설정 BEAN들 일일히 설정해 줄 수도 있음. 
```
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
```

## 3-2  @EnableWebMvc 설정.
- Annotation기반의 Spring MVC설정할 때 유용하게 사용하는 @EnableWebMvc
- @Configuration 어노테이션쪽에 사용.
- 유용한 점이. @Bean해서 처음부터 만들어주지 않아도, 조금씩 추가함으로써 바꿀 수 있음. Delegation <위임가능한 형태로 되어있음.
  1) @Configuration 파일에  `@EnableWebMvc` 등록.
  2) `context.setServletContext(servletContext);` 
```
        // @EnableWebMvc 사용할때 필수
        // context.setServletContext(servletContext);
```


