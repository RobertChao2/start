package cn.chao.maven;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@MapperScan(basePackages = {"cn.chao.maven.mapper","cn.chao.maven.customMapper"})
public class MavenApplication {

    public static void main(String[] args) {
        SpringApplication.run(MavenApplication.class, args);
    }

}
