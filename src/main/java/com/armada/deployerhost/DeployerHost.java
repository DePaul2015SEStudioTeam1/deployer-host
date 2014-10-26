package com.armada.deployerhost;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DeployerHost {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        DockerHostService helloService = context.getBean(DockerHostService.class);
        System.out.println(helloService.sayHello());
    }
}
