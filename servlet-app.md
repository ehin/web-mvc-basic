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
- 

