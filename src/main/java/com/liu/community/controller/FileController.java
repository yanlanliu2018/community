package com.liu.community.controller;

import com.liu.community.DTO.FileDTO;
import com.liu.community.provider.TencentCloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FileController {
    @Autowired
    private TencentCloudProvider tencentCloudProvider;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) {
//        public FileDTO upload (){
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartHttpServletRequest.getFile("editormd-image-file");
            try {
                String filepath = tencentCloudProvider.upload(file);
                FileDTO fileDTO = new FileDTO();
                fileDTO.setSuccess(1);
                fileDTO.setUrl(filepath);
                return fileDTO;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
//        FileDTO fileDTO = new FileDTO();
//        fileDTO.setSuccess(1);
//        fileDTO.setUrl("/images/wechat.jpg");
//        return fileDTO;
    }
}
