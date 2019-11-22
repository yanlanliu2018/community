package com.liu.community.mapper;

import com.liu.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question question);
    List<Question> selectRelated(Question question);
}