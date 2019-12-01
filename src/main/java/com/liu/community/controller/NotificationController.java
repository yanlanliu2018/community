package com.liu.community.controller;

import com.liu.community.DTO.NotificationDTO;
import com.liu.community.enums.NotificationTypeEnum;
import com.liu.community.model.User;
import com.liu.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String notification(HttpServletRequest request,
                               @PathVariable(name = "id") Long id){
        User user = (User)request.getSession().getAttribute("user");
        if (user==null){
            return "redict:/";
        }
        NotificationDTO notificationDTO = notificationService.read(id,user);
        if(NotificationTypeEnum.REPLY_QUESTION.getType()==notificationDTO.getType()
        ||NotificationTypeEnum.REPLY_COMMENT.getType()==notificationDTO.getType()){
            return "redirect:/question/"+notificationDTO.getOuterid();
        }else {
            return "redirect:/";
        }
    }
}
