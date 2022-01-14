package com.hello.newyear.qq.weixin.mq.aes;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;

public class WXTextMsg {
    private static DocumentBuilderFactory documentBuilderFactory = null;
    private String toUserName;
    private String fromUserName;
    private String createTime;
    private String msgType;
    private String content;
    private String msgId;

    @Override
    public String toString() {
        return "TextMsg [ToUserName=" + toUserName + ", FromUserName=" + fromUserName + ", CreateTime=" + createTime + ", MsgType=" + msgType + ", Content=" + content + "]";
    }

    public WXTextMsg(String toUserName, String fromUserName, String createTime, String msgType, String content) {
        this.toUserName = toUserName;
        this.fromUserName = fromUserName;
        this.createTime = createTime;
        this.msgType = msgType;
        this.content = content;
    }

    public WXTextMsg(String xmlStr) {
        if (documentBuilderFactory == null) {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
        }

        try {
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xmlStr.getBytes("UTF-8")));
            NodeList childNodes = document.getDocumentElement().getChildNodes();
            Class type = this.getClass();
            int length = childNodes.getLength();
            for (int i = 0; i < length; i++) {
                Node item = childNodes.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    String key = item.getNodeName();
                    String value = item.getTextContent();
                    Method declaredMethod = type.getDeclaredMethod("set" + key, String.class);
                    if (declaredMethod != null) {
                        declaredMethod.setAccessible(true);
                        declaredMethod.invoke(this, value);
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
