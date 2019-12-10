package com.liu.community.DTO;

import lombok.Data;

@Data
public class QuestionQueryDTO {
    private String search;
    private String tag;
    private Integer page;
    private Integer size;
}
