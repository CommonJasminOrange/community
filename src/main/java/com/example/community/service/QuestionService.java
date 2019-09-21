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
        Integer totalPage;

        Integer totalCount = questionMapper.count();



        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size +1;
        }

        if (page < 1){
            page = 1;
        }

        if(page > totalPage){
            page = totalPage;
        }

       paginationDto.setPagination(totalPage,page);

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

    public PaginationDto list(Integer userId, Integer page, Integer size) {
        PaginationDto paginationDto = new PaginationDto();

        Integer totalPage;

        Integer totalCount = questionMapper.countByUserId(userId);



        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size +1;
        }

        if (page < 1){
            page = 1;
        }

        if(page > totalPage){
            page = totalPage;
        }

        paginationDto.setPagination(totalPage,page);

        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.listByUserId(userId,offset,size);
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
