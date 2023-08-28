package hello.springmvc.basic.request;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyStringV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);

        response.getWriter().write("ok");
    }

    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);
        responseWriter.write("ok");
    }

    /**
     * Spring MVC 가 제공하는 파라미터
     *
     * HttpEntity
     * - HTTP header, body 정보 편리하게 조회
     * - 메시지 바디 정보 직접 조회
     * - 요청 파라미터 조회하는 기능과 관계 없음
     *      - url?key=value&key=value 이런게 요청 파라미터
     *      - @RequestParam, @ModelAttribute 와는 무관계
     *      - HttpEntity 메시지 자체를 가져오는 거임
     * - 응답에도 사용 가능
     *      - 메시지 바디 정보 직접 반환
     *      - 헤더 정보 포함 가능
     *      - 뷰 조회 안 함
     *
     * RequestEntity extends HttpEntity
     * ResponseEntity extends HttpEntity
     */
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(RequestEntity<String> httpEntity) throws IOException {
        String messageBody = httpEntity.getBody();
        log.info("messageBody={}", messageBody);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    /**
     * @RequestBody
     *
     * HTTP 메시지 바디 정보를 편리하게 조회 가능
     * 헤더 정보가 필요하면 HttpEntity 나 @RequestHeader 사용
     * 메시지 바디를 직접 조회하는 기능이므로, 요청 파라미터 조회하는 @RequestParam, @ModelAttribute 와는 상관 없다.
     */
    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) throws IOException {
        log.info("messageBody={}", messageBody);
        return "ok";
    }
}
