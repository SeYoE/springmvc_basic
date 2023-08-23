package hello.springmvc.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogTestController {

    /**
     * Log
     *
     * 쓰레드 정보, 클래스 이름 같은 부가 정보 확인 가능
     * 출력 모양 조정 가능
     * 로그 레벨에 따라 개발 서버, 운영 서버 로그 별로 상황에 맞게 조절 가능
     * 다양한 출력 형태가 가능 (시스템 아웃 콘솔, 파일, 네트워크, etc)
     * System.out 보다 좋은 성능 (내부 버퍼링, 멀티 스레드)
     *
     * -> 실무에서는 꼭 로그를 사용해야 한다.
     */
    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/log-test")
    public String logTest() {
        String name = "Spring";

        System.out.println("name = " + name);

        // 이거는 부적절한 로그 사용법
        // 미출력 시에도 메모리, CPU 등의 자원 소모
        log.info(" info log="+name);

        // 이게 적절한 로그 사용법
        log.trace("trace log={}", name);
        log.debug("debug log={}", name);
        log.info(" info log={}", name);
        log.warn(" warn log={}", name);
        log.warn("error log={}", name);

        return "ok";
    }
}
