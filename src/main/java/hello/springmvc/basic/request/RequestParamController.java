package hello.springmvc.basic.request;

import hello.springmvc.basic.HelloData;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username = {}, age = {}", username, age);

        response.getWriter().write("ok");
    }

    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String username,
            @RequestParam("age") int age
    ) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age
    ) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-v4")
    // request param 이 객체가 아닌 단순 타입이면 어노테이션 생략 가능
    public String requestParamV4(String username, int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-required")
    // request param 이 객체가 아닌 단순 타입이면 어노테이션 생략 가능
    public String requestParamRequired(
            // username 이 없는 경우에는 400 bad request
            @RequestParam(required = true) String username,
            // 빈 문자열과 Null 은 다른거임. username= -> 빈 문자열 -> 통과
            // int 타입으로 request param 에 아무것도 안 넣으면 nullable 한 타입에 null을 넣기 때문에 500 에러
            @RequestParam(required = false) Integer age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            // 빈 문자인 경우에도 defaultValue 가 적용됨
            // username= -> username = guest
            @RequestParam(defaultValue = "-1") Long userId,
//            @RequestParam(defaultValue = "guest") String username,
//            @RequestParam(defaultValue = "-1") Integer age,
            HttpServletRequest request) {
        log.info("parameter userId = {}", request.getParameter("userId"));
        log.info("real userId = {}", userId);
//        log.info("username={}, age={}", username, age);
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-map")
    // Map 도 되고, MultiValueMap 도 됨
    // Map -> (key:value)
    // MultiValueMap -> (key:[value1, value2, value3])
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }

    /**
     * @ModelAttribute
     * 1. HelloData 객체 생성
     * 2. 요청 파라미터 이름으로 HelloData 객체의 프로퍼티(getter, setter) 찾음
     *      찾은 setter 를 이용해서 파라미터의 값을 바인딩
     *     -> username 파라미터에 대해서 setUsername() 메소드를 찾아서 호출
     *
     * 바인딩 오류
     * 숫자가 들어가야 할 곳에 문자를 넣으면 BindException 발생
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
        log.info(helloData.toString());
        return "ok";
    }

    /**
     * @ModelAttribute 생략 가능
     * -> 근데 @RequestParam 도 생략 가능하잖아? 둘 중에 뭔지 어케 앎?
     *
     * String, int, Integer 같은 단순 타입 -> @RequestParam
     * 나머지 -> @ModelAttribute (argument resolver 로 지정해둔 타입 외)
     */
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) {
        log.info(helloData.toString());
        return "ok";
    }
}
