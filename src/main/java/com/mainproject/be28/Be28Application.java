package com.mainproject.be28;

import com.mainproject.be28.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableJpaAuditing
@EnableSwagger2
@ServletComponentScan
public class Be28Application {

    public static void main(String[] args) {
        SpringApplication.run(Be28Application.class, args);
    }
  


}
