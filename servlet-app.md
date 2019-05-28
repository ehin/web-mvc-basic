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

-Filter구현 
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


