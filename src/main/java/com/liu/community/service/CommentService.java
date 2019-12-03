package com.liu.community.service;

import com.liu.community.DTO.CommentDTO;
import com.liu.community.enums.CommentTypeEnum;
import com.liu.community.enums.NotificationStatusEnum;
import com.liu.community.enums.NotificationTypeEnum;
import com.liu.community.exception.CustomizeErrorCode;
import com.liu.community.exception.CustomizeException;
import com.liu.community.mapper.*;
import com.liu.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment,User commentator){
        if (comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_NOT_FOUND);
        }
        if (comment.getType()==null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            //评论所属问题
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);
            //增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setConentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            //增加新回复
            createNotify(comment, dbComment.getCommentator(),commentator.getName(),
                    question.getTitle(),NotificationTypeEnum.REPLY_COMMENT.getType(),question.getId());
        }else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            comment.setConentCount(0);
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            //增加新回复
            createNotify(comment, question.getCreator(),commentator.getName(),
                    question.getTitle(),NotificationTypeEnum.REPLY_QUESTION.getType(),question.getId());
        }
    }

    private void createNotify(Comment comment, Long receiver,String notifierName,String outerTitle,Integer type,Long outerId) {
        if(receiver == comment.getCommentator()){
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notification.setNotifier(comment.getCommentator());
        notification.setType(type);
        notification.setOuterid(outerId);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Long id, Integer type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type);
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0){
            return new ArrayList<>();
        }
        //获取去重的评论人id
        Set<Long> commentators = comments.stream()
                .map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        //获取评论人并转化为map
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long,User> userMap = users.stream().
                collect(Collectors.toMap(user->user.getId(),user->user));

        //转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
