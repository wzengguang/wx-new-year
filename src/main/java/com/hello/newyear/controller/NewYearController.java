package com.hello.newyear.controller;

import com.hello.newyear.WXData;
import com.hello.newyear.qq.weixin.mq.aes.WXTextMsg;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class NewYearController {

    @GetMapping(value = {"/new-year", "/"})
    public String hello(@RequestParam(name = "name", required = false, defaultValue = "Happy New Year!") String name, Model model) {
        model.addAttribute("name", name);
        return "newYear";
    }


    @GetMapping("hellos")
    @ResponseBody
    public List<WXTextMsg> getHellos() {
        return WXData.wxTextMsgs;
    }
}
