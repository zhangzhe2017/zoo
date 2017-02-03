package weixin.connection.web;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
