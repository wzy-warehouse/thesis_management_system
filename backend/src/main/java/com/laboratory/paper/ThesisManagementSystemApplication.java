package com.laboratory.paper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 论文管理系统后台启动入口
 * 作为Spring Boot应用的入口点，负责启动整个后端服务
 */
@SpringBootApplication
@MapperScan("com.laboratory.paper.mapper") // 扫描MyBatis的Mapper接口
public class ThesisManagementSystemApplication {

    // 使用非静态变量
    @Value("${server.port}")
    private Integer port;

    public static void main(String[] args) {
        // 启动Spring Boot应用并获取应用上下文
        var context = SpringApplication.run(ThesisManagementSystemApplication.class, args);

        // 从上下文中获取当前实例
        ThesisManagementSystemApplication app = context.getBean(ThesisManagementSystemApplication.class);

        System.out.println("论文管理系统后端服务启动成功！访问地址: http://localhost:" + app.port);
    }
}