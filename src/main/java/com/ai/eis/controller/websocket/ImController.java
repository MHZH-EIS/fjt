package com.ai.eis.controller.websocket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.ai.eis.common.AjaxResult;
import com.ai.eis.common.SocketMessage;
import com.ai.eis.handler.WebSocketHandler;
import com.ai.eis.model.Member;

 

@Controller
public class ImController {

    @Autowired
    WebSocketHandler webSocketHandler;

    /**
     * 发送信息给指定用户
     *
     * @param uid
     * @param content
     * @param member
     */
    @RequestMapping("/im/send")
    @ResponseBody
    public void send(Integer uid, String content, @SessionAttribute("s_member") Member member) {
        Map<String, Object> message = new HashMap<>();
        message.put("fromUid", member.getUserid());
        message.put("realName", member.getRealName());
        message.put("message", content);
        webSocketHandler.sendMessageToUser(uid, new SocketMessage("message", message).toTextMessage());
    }

    /**
     * 获取在线用户列表
     *
     * @return
     */
    @RequestMapping("/im/user/list")
    @ResponseBody
    public AjaxResult userList() {
        return new AjaxResult().setData(webSocketHandler.getUsers());
    }
}
