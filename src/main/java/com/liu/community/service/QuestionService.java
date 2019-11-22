package com.liu.community.service;

import com.liu.community.DTO.PaginationDTO;
import com.liu.community.DTO.QuestionDTO;
import com.liu.community.exception.CustomizeErrorCode;
import com.liu.community.exception.CustomizeException;
import com.liu.community.mapper.QuestionExtMapper;
import com.liu.community.mapper.QuestionMapper;
import com.liu.community.mapper.UserMapper;
import com.liu.community.model.Question;
import com.liu.community.model.QuestionExample;
import com.liu.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO list(Integer page, Integer size){

        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;

        Integer totalCount = (int)questionMapper.countByExample(new QuestionExample());

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
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questionList =
                questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample,new RowBounds(offset,size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question:questionList){
            User user = userMapper.selectByPrimaryKey(question.getCreator());

            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);

            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestionDTOS(questionDTOList);

        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPage;

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);

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
        questionExample.createCriteria().andCreatorEqualTo(userId);
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds
                (questionExample,new RowBounds(offset,size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question:questionList){
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        paginationDTO.setQuestionDTOS(questionDTOList);

        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        Question dbquestion = questionMapper.selectByPrimaryKey(question.getId());
        if (dbquestion == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else {
            Question updateQusetion = new Question();
            updateQusetion.setGmtModified(System.currentTimeMillis());
            updateQusetion.setTag(question.getTag());
            updateQusetion.setDescription(question.getDescription());
            updateQusetion.setTitle(question.getTitle());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(updateQusetion,questionExample);
            if (update!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id){
        Question question = new Question();
        question.setViewCount(1);
        question.setId(id);
        questionExtMapper.incView(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if(StringUtils.isBlank(queryDTO.getTag())){
            return new ArrayList<>();
        }
        String questiontag = queryDTO.getTag().replace(',', '|');
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(questiontag);

        List<Question> questionList = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOList = questionList.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOList;
    }
}
