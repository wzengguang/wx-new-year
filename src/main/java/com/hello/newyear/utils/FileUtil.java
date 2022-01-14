package com.hello.newyear.utils;

import com.alibaba.fastjson.JSON;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    public void writeJson(File systemSetting, Object o) {
        String jsonString = JSON.toJSONString(o);
        writeString(systemSetting, jsonString);
    }

    public void writeString(File file, String content) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write(content);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
