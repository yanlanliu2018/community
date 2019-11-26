package com.liu.community.cache;

import com.liu.community.DTO.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
    public static List<TagDTO> get(){
        List<TagDTO> tagDTOS = new ArrayList<>();

        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("javascript","php","java","c++","python","c"));
        tagDTOS.add(program);

        TagDTO framwork = new TagDTO();
        framwork.setCategoryName("开发框架");
        framwork.setTags(Arrays.asList("spring","express","flask","yii","koa"));
        tagDTOS.add(framwork);

        TagDTO db = new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("mysql","redis","sql","mongodb","oracle","sqlite"));
        tagDTOS.add(db);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("git","github","viso","eclipse","idea"));
        tagDTOS.add(tool);

        return tagDTOS;
    }

    public static String filterInvalid(String tags){
        String[] split = StringUtils.split(tags,',');
        List<TagDTO> tagDTOS = get();

        List<String> tagList = tagDTOS.stream().flatMap
                (tagDTO->tagDTO.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t->!tagList.contains(t)).
                collect(Collectors.joining(","));
        return invalid;
    }
}
