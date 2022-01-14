package com.hello.newyear.controller;

import com.hello.newyear.WXData;
import com.hello.newyear.qq.weixin.mq.aes.AesException;
import com.hello.newyear.qq.weixin.mq.aes.WXBizMsgCrypt;
import com.hello.newyear.qq.weixin.mq.aes.WXTextMsg;
import com.hello.newyear.qq.weixin.mq.aes.WeiXinParamesUtil;
import org.springframework.web.bind.annotation.*;

@RestController
public class WXCommunicationController {

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

        } catch (AesException e) {
            e.printStackTrace();
        }

        if (msg != null) {
            WXData.wxTextMsgs.add(msg);
        }

        return true;
    }
}
