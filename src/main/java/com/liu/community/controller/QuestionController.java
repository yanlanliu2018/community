package com.liu.community.controller;

import com.liu.community.DTO.CommentDTO;
import com.liu.community.DTO.QuestionDTO;
import com.liu.community.enums.CommentTypeEnum;
import com.liu.community.service.CommentService;
import com.liu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long  id, Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> questionDTOList = questionService.selectRelated(questionDTO);
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.QUESTION.getType());
//        累加阅读数
        questionService.incView(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", commentDTOS);
        model.addAttribute("relatedQuestions", questionDTOList);
        return "question";
    }
}
