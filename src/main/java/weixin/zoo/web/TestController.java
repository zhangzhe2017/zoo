package weixin.zoo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by viczhang.zhangz on 2016/6/23.
 */
@Controller
public class TestController {


    @RequestMapping("/list.html")
    public String home() {
        return "list";
    }

    @RequestMapping("/detail.html")
    public String detail() { return "detail";}

}
