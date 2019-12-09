##liu-community

##部署
-git 
-JDK
-Maven
-MySQL
(备注：因为是SpringBoot项目，这里不需要安装Tomcat)

##步骤
- yum update
- yum install git
- mkdir app
- cd app
- git clone https://github.com/yanlanliu2018/community.git
- yum install maven
- mvn -v
- pwd
- mvn compile package
- more src/main/resources/application.properties
- cp src/main/resources/application.properties src/main/resources/application-production.properties
- vim src/main/resources/application-production.properties
- mvn package
- java -jar -Dspring.profiles.active=production target/community-0.1.0.jar
- ps -aux | grep java
- mvn clean compile flyway:migrate





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
[icon 图标](https://www.iconfont.cn/)

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