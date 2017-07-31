package weixin.zoo.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import weixin.zoo.infrastructure.model.Form;
import weixin.zoo.infrastructure.model.PageDto;
import weixin.zoo.infrastructure.model.User;
import weixin.zoo.service.FormService;
import weixin.zoo.service.UserService;
import weixin.zoo.utils.ActivityTypeEnum;
import weixin.zoo.utils.ResultUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viczhang.zhangz on 2017/7/31.
 */
@Controller
@RequestMapping("/list")
public class DataController {

    @Autowired
    public UserService userService;

    @Autowired
    private FormService formService;

    @RequestMapping("/getSquareList")
    @ResponseBody
    public String getSquareList(HttpServletRequest request){
        String permission = request.getParameter("permission");
        String type = request.getParameter("type");

        List<String> userIds = new ArrayList<String>();
        try {
            List<User> users = userService.getUsersByPermission(permission);
            for(User user: users){
                userIds.add(user.getUserId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Integer currentPage = Integer.parseInt(request.getParameter("currentPage"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));

        PageDto pageDtoSearch = new PageDto(1, -1);

        List<Form> formsRaws = formService.getFormsByUserId(userIds, ActivityTypeEnum.ACTIVITY.getName(), pageDtoSearch);

        List<Form> forms = new ArrayList<Form>();
        JSONArray jsonArray = new JSONArray();
        for(Form form : formsRaws){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",form.getId());
            jsonObject.put("title", form.getFormName());
            jsonObject.put("type", ActivityTypeEnum.ACTIVITY.getName());
            jsonObject.put("formOwner", form.getFormOwner());

            try{
                JSONObject json = (JSONObject)JSONObject.parse(form.getFormValue());
                String pic = "";
                if(!JSONObject.parseArray(json.getString("cover")).isEmpty()){
                    pic = JSONObject.parseArray(json.getString("cover")).getString(0);
                }else{
                    pic = "http://zujuguan.oss-cn-shanghai.aliyuncs.com/logo.png";
                }

                String startTime = json.getString("startTime");
                String endTime = json.getString("endTime");
                Long endTimeDate = Long.parseLong(endTime);
                String status = "";
                if(type.equals("1")){
                    if(System.currentTimeMillis() - endTimeDate > 0){
                        forms.add(form);
                        status = new String("�ѽ�ֹ".getBytes("utf-8"),"UTF-8");
                    }
                }else{
                    if(System.currentTimeMillis() - endTimeDate <= 0){
                        forms.add(form);
                        status =  new String("������".getBytes("utf-8"),"utf-8");
                    }
                }
                jsonObject.put("status",status);
                jsonObject.put("pic", pic);
                jsonObject.put("startTime",Long.parseLong(startTime));

            }catch (Exception e){
                e.printStackTrace();
            }

            jsonArray.add(jsonObject);
        }
        //代码分页
        List<Object> jsonArrayPaged = jsonArray.subList(pageSize * (currentPage - 1), pageSize * currentPage);


        return ResultUtils.assembleResult(true, "true", jsonArrayPaged);
    }
}
