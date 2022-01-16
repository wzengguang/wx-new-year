package com.hello.newyear.service;

import com.hello.newyear.entity.WXUserResponse;

public interface WXService {

    String getAccessToken();

    WXUserResponse GetUserByWXUserId(String userId);
}
