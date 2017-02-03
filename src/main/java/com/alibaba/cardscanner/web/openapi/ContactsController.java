package com.alibaba.cardscanner.web.openapi;

import com.alibaba.cardscanner.service.openapi.OApiException;
import com.alibaba.cardscanner.service.openapi.auth.AuthHelper;
import com.alibaba.cardscanner.service.openapi.department.Department;
import com.alibaba.cardscanner.service.openapi.department.DepartmentHelper;
import com.alibaba.cardscanner.service.openapi.user.User;
import com.alibaba.cardscanner.service.openapi.user.UserHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viczhang.zhangz on 2016/6/23.
 */

@Controller
public class ContactsController {

    @RequestMapping("/contacts")
    public void contacts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String corpId = request.getParameter("corpid");

        try {
            response.setContentType("text/html; charset=utf-8");

            String accessToken = AuthHelper.getAccessToken(corpId);

            List<Department> departments = new ArrayList<Department>();
            departments = DepartmentHelper.listDepartments(accessToken);
            JSONObject json = new JSONObject();
            JSONArray usersArray = new JSONArray();


            System.out.println("depart num:"+departments.size());
            for(int i = 0;i<departments.size();i++){
                JSONObject userDepJSON = new JSONObject();

                JSONObject usersJSON = new JSONObject();
                JSONArray userArray = new JSONArray();

                System.out.println("dep:"+departments.get(i).toString());
                List<User> users = new ArrayList<User>();
                users = UserHelper.getDepartmentUser(accessToken, Long.valueOf(departments.get(i).id));
                if(users.size()==0){
                    continue;
                }
                for(int j = 0;j<users.size();j++){
                    String user = JSON.toJSONString(users.get(j));
                    userArray.add(JSONObject.parseObject(user, User.class));
                }
                System.out.println("user:"+userArray.toString());
                usersJSON.put("name", departments.get(i).name);
                usersJSON.put("member", userArray);
                usersArray.add(usersJSON);
            }
            json.put("department", usersArray);
            System.out.println("depart:"+json.toJSONString());
            response.getWriter().append(json.toJSONString());

        } catch (OApiException e) {
            e.printStackTrace();
            response.getWriter().append(e.getMessage());
        }
    }
}
