package com.liu.community.mapper;

import com.liu.community.model.Question;

public interface QuestionExtMapper {
    int incView(Question record);
    int incCommentCount(Question question);
}