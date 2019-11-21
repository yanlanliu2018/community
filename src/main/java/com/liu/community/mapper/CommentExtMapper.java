package com.liu.community.mapper;

import com.liu.community.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}