package com.liu.community.exception;

public enum  CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001,"你找的问题不在了，要不换个试试？"),
    TARGET_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"当前操作未登录，请登陆后重试"),
    SYS_ERROR(2004,"服务器冒烟了，要不然你稍后试试！"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或者不存在"),
    COMMENT_NOT_FOUND(2006,"回复的评论不存在，要不换个试试？"),
    COMMENT_IS_EMPTY(2007,"输入的内容不能为空!"),
    READ_NOTIFICATION_FAIL(2008,"你在都别人的信息哦~"),
    NOTIFICATION_NOT_FOUND(2009,"消息不见了！"),
    FILE_UPLOAD_FAIL(2010,"图片上传失败！"),
    ;

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code,String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }


}
