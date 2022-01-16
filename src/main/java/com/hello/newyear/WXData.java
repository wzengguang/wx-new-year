package com.hello.newyear;

import com.hello.newyear.entity.WXUserResponse;
import com.hello.newyear.qq.weixin.mq.aes.WXTextMsg;

import java.util.*;


public class WXData {

    public static List<WXTextMsg> wxTextMsgs = new ArrayList<>();

    public static Map<String, WXUserResponse> users = new HashMap<>();
}
