##liu-community
##资料

[Spring 文档](https://spring.io/guides)  
[Spring Web](https://spring.io/guides/gs/serving-web-content/)  
[es](https://elasticsearch.cn)  
[Build GitHub OAuth APP](https://developer.github.com/apps/building-oauth-apps/)  
[BootStrap](https://v3.bootcss.com/getting-started/)  
[thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html)  
[Spring Boot Reference Guide](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#using-boot-devtools)  
[web on sevlet stack](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)
[MarkDown 插件](http://editor.md.ipandao.com/)

##工具
[Git](http://gti-scm.com/dowload)  
[Visual-Paradigm](https://www.visual-paradigm.com)  
[Lombok](https://projectlombok.org/)


##脚本
''' sql  
CREATE  TABLE USER
(
    ACCOUNT_ID VARCHAR(100),
    NAME VARCHAR(50),
    TOKEN CHARACTER(36),
    GMT_CREATE BIGINT,
    GMT_MODIFIED BIGINT,
    ID INT DEFAULT () AUTO_INTCREMENT PRIMATY KEY NOT NULL 
)

'''

'''bash  
mvn flyway:migrate


mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate  
'''