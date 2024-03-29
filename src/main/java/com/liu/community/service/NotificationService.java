package com.liu.community.service;

import com.liu.community.DTO.NotificationDTO;
import com.liu.community.DTO.PaginationDTO;
import com.liu.community.DTO.QuestionDTO;
import com.liu.community.enums.NotificationStatusEnum;
import com.liu.community.enums.NotificationTypeEnum;
import com.liu.community.exception.CustomizeErrorCode;
import com.liu.community.exception.CustomizeException;
import com.liu.community.mapper.NotificationMapper;
import com.liu.community.mapper.UserMapper;
import com.liu.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();

        Integer totalPage;

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        notificationExample.setOrderByClause("gmt_create desc");
//        andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        Integer totalCount = (int)notificationMapper.countByExample(notificationExample);

        if (totalCount%size == 0){
            totalPage = totalCount/size;
        }else {
            totalPage = totalCount/size + 1;
        }

        if (page<1){
            page = 1;
        }

        if (page>totalPage){
            page = totalPage;
        }

        paginationDTO.setPagination(totalPage,page);


        Integer offset = size*(page-1);
        List<Notification> notificationList = notificationMapper.selectByExampleWithRowbounds
                (notificationExample,new RowBounds(offset,size));

        if (notificationList.size()==0){
            return paginationDTO;
        }

        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        for (Notification notification : notificationList){
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }

        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }

    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId)
        .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);

    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(user.getId(),notification.getReceiver())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;

    }
}
