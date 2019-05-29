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
#### DispactherServlet : 
- dispatcherServlet은 공유해서 쓸 수가 없음.



