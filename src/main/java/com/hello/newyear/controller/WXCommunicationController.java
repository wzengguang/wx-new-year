package com.hello.newyear.controller;

import com.hello.newyear.WXData;
import com.hello.newyear.qq.weixin.mq.aes.AesException;
import com.hello.newyear.qq.weixin.mq.aes.WXBizMsgCrypt;
import com.hello.newyear.qq.weixin.mq.aes.WXTextMsg;
import com.hello.newyear.qq.weixin.mq.aes.WeiXinParamesUtil;
import com.hello.newyear.service.WXService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class WXCommunicationController {
    Logger logger = LoggerFactory.getLogger(WXCommunicationController.class);
    @Autowired
    WXService wxService;

    @GetMapping("access_token")
    public String getAccessToken() {
        return wxService.getAccessToken();
    }

    @GetMapping("receive")
    public String receiveVerify(@RequestParam(value = "msg_signature", defaultValue = "") String msg_signature,
                                @RequestParam(value = "timestamp", defaultValue = "") String timestamp,
                                @RequestParam(value = "nonce", defaultValue = "") String nonce,
                                @RequestParam(value = "echostr", defaultValue = "") String echostr) {

        String result = null;
        try {
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(WeiXinParamesUtil.token, WeiXinParamesUtil.encodingAESKey, WeiXinParamesUtil.corpId);
            result = wxcpt.VerifyURL(msg_signature, timestamp, nonce, echostr);
        } catch (AesException e) {
            e.printStackTrace();
            logger.error("receiveVerify error:", e);
        }
        return result;
    }

    @PostMapping("receive")
    public boolean receiveMsg(@RequestParam(value = "msg_signature") String msg_signature,
                              @RequestParam(value = "timestamp") String timestamp,
                              @RequestParam(value = "nonce") String nonce,
                              @RequestBody String body) {

        WXTextMsg msg = null;
        try {
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(WeiXinParamesUtil.token, WeiXinParamesUtil.encodingAESKey, WeiXinParamesUtil.corpId);

            String result = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, body);

            msg = new WXTextMsg(result);
            msg.setUserNameInCorp(wxService.GetUserByWXUserId(msg.getFromUserName()).getName());
            logger.info("receiveMsg info::" + msg.Log());
        } catch (AesException e) {
            e.printStackTrace();
            logger.error("receiveMsg error:", e);
        }

        if (msg != null) {
            WXData.wxTextMsgs.add(msg);
        }

        return true;
    }
}
