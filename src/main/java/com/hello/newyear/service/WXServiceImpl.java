package com.hello.newyear.service;


import com.hello.newyear.WXData;
import com.hello.newyear.controller.WXCommunicationController;
import com.hello.newyear.entity.AccessTokenResponse;
import com.hello.newyear.entity.WXUserResponse;
import com.hello.newyear.qq.weixin.mq.aes.WeiXinParamesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class WXServiceImpl implements WXService {
    Logger logger = LoggerFactory.getLogger(WXServiceImpl.class);
    @Autowired
    RestTemplate restTemplate;

    private AccessTokenResponse accessTokenResponse = null;
    private long expiredTime = 0;

    public String getAccessToken() {
        if (accessTokenResponse == null || new Date().getTime() > expiredTime) {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + WeiXinParamesUtil.corpId + "&corpsecret=" + WeiXinParamesUtil.corpSecret;
            try {
                AccessTokenResponse response = restTemplate.getForObject(url, AccessTokenResponse.class);
                if (response.getErrcode() == 0) {
                    accessTokenResponse = response;
                    expiredTime = new Date().getTime() + (response.getExpires_in() - 10) * 1000L;
                    logger.info("getAccessToken::" + accessTokenResponse.getAccess_token());
                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.error("getAccessToken error:", e);
            }
        }

        return accessTokenResponse.getAccess_token();
    }

    public WXUserResponse GetUserByWXUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            return null;
        }

        if (WXData.users.containsKey(userId)) {
            return WXData.users.get(userId);
        }

        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=" + getAccessToken() + "&userid=" + userId;
        WXUserResponse user = null;
        try {
            WXUserResponse response = restTemplate.getForObject(url, WXUserResponse.class);
            if (response.errcode == 0) {
                user = response;
                WXData.users.put(userId, user);
                logger.info("WXUserResponse::" + userId + ":" + user.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("GetUserByWXUserId error:", e);
        }

        return user;
    }
}
