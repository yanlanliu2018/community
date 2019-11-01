package com.liu.community.controller;

import com.liu.community.DTO.AccessTokenDTO;
import com.liu.community.DTO.GithubUser;
import com.liu.community.mapper.UserMapper;
import com.liu.community.model.User;
import com.liu.community.provider.GitHubProvider;
import com.liu.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GitHubProvider gitHubProvider;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state,
                           HttpServletRequest request
                           ,HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirectUri);
        String accessToken = gitHubProvider.grtAccessToken(accessTokenDTO);
        GithubUser githubUser = gitHubProvider.getUser(accessToken);
        if (githubUser != null && githubUser.getId()!=null){
            User user = new User();
            user.setName(githubUser.getName());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAccountId(githubUser.getId().toString());
            userService.createOrUpdate(user);
//            userMapper.insert(user);
//            request.getSession().setAttribute("user", githubUser);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else {
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
