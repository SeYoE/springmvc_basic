package hello.springmvc.basic.response;

import hello.springmvc.basic.HelloData;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @ResponseBody
 * - Http body 에 문자 내용을 직접 변환
 * - view resolver 대신에 HttpMessageConverter 동작
 * - 기본 문자 처리 : StringHttpMessageConverter
 * - 기본 객체 처리 : MappingJackson2HttpMessageConverter
 * - byte 처리 등 기타 여러 HttpMessageConverter 가 등록되어 있음
 * - 응답의 경우 클라이언트의 Http accept 헤더와 서버의 컨트롤러 반환 타입 정보를 조합해서 컨버터 선택
 *
 * Spring MVC 는 다음 경우에 HTTP 메시지 컨버터 적용
 * 1. HTTP 요청 : @RequestBody, HttpEntity(RequestEntity)
 * 2. HTTP 응답 : @ResponeBody, HttpEntity(ResponseEntity)
 *
 * 종류
 * ByteArrayHttpMessageConverter        -> 1순위. byte[]
 * StringHttpMessageConverter           -> 2순위. String
 * MappingJackson2HttpMessageConverter  -> 3순위. 객체 혹은 HashMap
 *
 * 그래서 Spring MVC의 어디?
 *
 * @RequestMapping 을 처리하는 핸들러 어뎁터 RequestMappingHandlerAdatper(요청 매핑 핸들러 어뎁터)
 * - RequestMappingHandlerAdatper 는 ArgumentResolver 호출, ReturnValueHandler 수신
 *
 * ArgumentResolver
 * - 컨트롤러의 파라미터, 어노테이션 정보 기반으로 전달할 데이터 생성
 * - 아래 처럼 매우 다양한 파라미터를 유연하게 처리할 수 있는 근간
 * - supportsParameter() 메소드로 파라미터 지원 여부를 판단
 * - HttpServletRequest, Model, @RequestParam, @ModelAttribute, @RequestBody, HttpEntity
 *
 * ReturnValueHanlder
 * - 컨트롤러의 반환 값을 변환
 * - 컨트롤러에서 String 으로 뷰 이름을 반환해도 동작하는 근간
 * - ModelAndView, @ResponseBody, HttpEntity
 *
 * *HTTP message Converter*
 * - ArgumentResolver 가 사용
 * - 요청의 경우
 *  - @RequestBody 처리하는 ArgumentResolver, HttpEntity 처리하는 ArgumentResolver 존재
 *  - 각 ArgumentResolver 들이 HTTP 메시지 컨버터 사용해 필요한 객체 생성
 * - 응답의 경우
 *  - @ResponseBody, HttpEntity 처리하는 ReturnValueHandler 존재
 *  - 여기에서 메시지 컨버터 호출해서 응답 생성
 */
@Slf4j
//@Controller
//@ResponseBody
@RestController // = @Controller + @ResponseBody
public class ResponseBodyController {

    /**
     * 서블릿을 직접 다룰 때처럼 HttpServletResponse 객체 통해서 HTTP 메시지 바디에 직접 응답 메시지 전달
     */
    @GetMapping("/response-body-string-v1")
    public void responseBodyV1(HttpServletResponse response) throws IOException {
        response.getWriter().write("ok");
    }

    /**
     * ResponseEntity 는 HttpEntity 상속 구현체
     * HttpEntity 는 메시지 헤더, 바디 정보 가짐.
     * ResponseEntity 는 여기에 추가로 HTTP 응답 코드 설정 가능
     */
    @GetMapping("/response-body-string-v2")
    public ResponseEntity<String> responseBodyV2() {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    /**
     * @ResponseBody 사용하면 view 사용하지 않고, HTTP 메시지 컨버터를 통해 HTTP 메시지를 직접 입력
     * ResponseEntity 와 같은 동작
     */
//    @ResponseBody
    @GetMapping("/response-body-string-v3")
    public String responseBodyV3() {
        return "ok";
    }

    /**
     * ResponseEntity 반환
     * HTTP 메시지 컨버터 통해 JSON 형식으로 변환되어 반환
     */
    @GetMapping("/response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyJsonV1() {
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);

        return new ResponseEntity<>(helloData, HttpStatus.OK);
    }

    /**
     * @ResponseBody 사용하면 HTTP 응답 코드 설정 어려움
     * 이때 @ResponseStatus 사용해서 응답 코드 설정
     * 다만, 동적으로 응답코드를 변경할 수는 없음.
     */
    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
    @GetMapping("/response-body-json-v2")
    public HelloData responseBodyJsonV2() {
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
        return helloData;
    }

}
