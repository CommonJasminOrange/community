package com.example.community.controller;

import com.example.community.dto.AccessTokenDto;
import com.example.community.dto.GithubUser;
import com.example.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request
    ){
       AccessTokenDto accessTokenDto = new AccessTokenDto();
       accessTokenDto.setCode(code);
       accessTokenDto.setRedirect_url(redirectUri);
       accessTokenDto.setClient_id(clientId);
       accessTokenDto.setClient_secret(clientSecret);
       accessTokenDto.setState(state);
       String accessToken = githubProvider.getAccessToken(new AccessTokenDto());
       GithubUser user = githubProvider.getUser(accessToken);
        if(user != null){
            //登录成功，写cookie和session
            request.getSession().setAttribute("user",user);
            return "redirect:/";

        }else{
            //登录失败，重新登录
            return "redirect:/";
        }


    }
}
