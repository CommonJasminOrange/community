package com.example.community.service;

import com.example.community.dto.PaginationDto;
import com.example.community.dto.QuestionDto;
import com.example.community.mapper.QuestionMapper;
import com.example.community.mapper.UserMapper;
import com.example.community.model.Question;
import com.example.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {


    @Autowired
    private  QuestionMapper questionMapper;

    @Autowired
    private  UserMapper userMapper;

    public PaginationDto list(Integer page, Integer size) {

        PaginationDto paginationDto = new PaginationDto();
        Integer totalCount = questionMapper.count();
        paginationDto.setPagination(totalCount,page,size);

        if (page < 1){
            page = 1;
        }

        if(page > paginationDto.getTotalPage()){
            page = paginationDto.getTotalPage();
        }


        Integer offset = size * (page - 1);
       List<Question> questions = questionMapper.list(offset,size);
       List<QuestionDto> questionDtoList = new ArrayList<>();

        for (Question question: questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            //questionDto.setId(question.getId()); //古老方法
            BeanUtils.copyProperties(question,questionDto); //spring内置方法
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }

        paginationDto.setQuestions(questionDtoList);
        return paginationDto;
    }
}
