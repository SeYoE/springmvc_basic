package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1() {
        return new ModelAndView("response/hello")
                .addObject("data", "hello!");
    }

    /**
     * @RestController, @ResponseBody 어노테이션을 사용하면,
     * return String 에 해당하는 view 가 렌더링되는게 아니라, 문자열 자체가 응답 바디로 나감.
     * view resolver 실행하지 않고 메시지 바디에 직접 문자열을 넣어주기 때문
     */
    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) {
        model.addAttribute("data", "hello!");
        // view 논리 이름을 넣어주면 해당 경로의 뷰 템플릿이 렌더링됨.
        return "response/hello";
    }

    /**
     * 비추 방법임.
     *
     * 1. return type void
     * 2. @Controller 컨트롤러
     * 3. HttpServletResponse, OutputStream(Writer) 같은 HTTP 메시지 바디 처리 파라미터 부재
     *
     * 요청 URL 참고해서 논리 뷰 이름으로 사용함.
     */
    @RequestMapping("/response/hello")
    public void responseViewV3(Model model) {
        model.addAttribute("data", "hello!");
    }
}
