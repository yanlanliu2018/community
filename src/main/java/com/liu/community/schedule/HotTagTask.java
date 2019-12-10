package com.liu.community.schedule;

import com.liu.community.cache.HotTagCache;
import com.liu.community.mapper.QuestionMapper;
import com.liu.community.model.Question;
import com.liu.community.model.QuestionExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class HotTagTask {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private HotTagCache hotTagCache;

    @Scheduled(fixedRate = 5000)
    public void hotTagSchedule(){
        int offset = 0;
        int limit = 20;
        List<Question> questionList = new ArrayList<>();
        Map<String,Integer> priorities = new HashMap<>();
        while (offset==0||questionList.size()==limit){
            questionList = questionMapper.selectByExampleWithRowbounds
                    (new QuestionExample(), new RowBounds(offset,limit));
            for (Question question : questionList){
                String[] tags = StringUtils.split(question.getTag(), ",");
                for (String tag:tags){
                    Integer priority = priorities.get(tag);
                    if(priority!=null){
                        priorities.put(tag, priority+5+question.getCommentCount());
                    }else {
                        priorities.put(tag, 5+question.getCommentCount());
                    }
                }

            }
            offset+=limit;
        }
        hotTagCache.updateTags(priorities);

    }
}
