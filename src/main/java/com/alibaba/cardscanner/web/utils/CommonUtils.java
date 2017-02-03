package com.alibaba.cardscanner.web.utils;

import com.alibaba.cardscanner.service.openapi.OApiException;
import com.alibaba.cardscanner.service.openapi.auth.AuthHelper;
import com.alibaba.cardscanner.service.openapi.user.User;
import com.alibaba.cardscanner.service.openapi.user.UserHelper;

/**
 * Created by viczhang.zhangz on 2016/6/29.
 */
public class CommonUtils {
    private static String SEPARATE = "_";

    /*
     * 根据code & corpid 获取用户信息
     */
    public static User praseUserInfo(String code, String corpId) {
        try {
            String accessToken = AuthHelper.getAccessToken(corpId);
            System.out.println("access token:"+accessToken);
            return UserHelper.getUser(accessToken, UserHelper.getUserInfo(accessToken, code).getString("userid"));
        } catch (OApiException e) {
            e.printStackTrace();
            throw new RuntimeException("find user failed.");
        }
//        return null;
    }

    public static String pieceUserId(String corpId, String userId){
        return corpId + SEPARATE + userId;
    }
}
